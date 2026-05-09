package es.sebas1705.couchbase.manager

import com.couchbase.lite.Collection
import com.couchbase.lite.DataSource
import com.couchbase.lite.Database
import com.couchbase.lite.Expression
import com.couchbase.lite.ListenerToken
import com.couchbase.lite.MutableDocument
import com.couchbase.lite.QueryBuilder
import com.couchbase.lite.SelectResult
import es.sebas1705.analytics.datasources.LogEventDataSource
import es.sebas1705.common.managers.ClassLogData
import es.sebas1705.common.utlis.extensions.types.logE
import es.sebas1705.couchbase.config.alias.DocMap
import javax.inject.Inject

/**
 * CouchbaseManager is responsible for managing Couchbase database operations.
 * It provides methods to select, insert, replace, delete documents, and register/unregister listeners.
 *
 * @property database [Database] The Couchbase database instance.
 * @property logEventDataSource [LogEventDataSource] Used for logging events.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
internal class CouchbaseManager @Inject constructor(
    private val database: Database,
    private val logEventDataSource: LogEventDataSource
) : ClassLogData(), ICouchbaseManager {

    // Properties:
    private var collectionsTokens: MutableMap<String, ListenerToken?> = HashMap()
    private val documentsTokens: MutableMap<String, ListenerToken?> = HashMap()

    /**
     * Selects:
     */

    override fun getAll(
        collectionName: String,
    ): List<DocMap> = try {
        val collection = getAndCreateIfNotExist(collectionName)
        val query = QueryBuilder.select(SelectResult.all())
            .from(DataSource.collection(collection))
        val result = query.execute()
        result.allResults().mapNotNull { it.getDictionary(collectionName)?.toMap() }
    } catch (e: Exception) {
        logError("Error getting all in $collectionName: ${e.message}")
        emptyList()
    }

    override fun get(
        collectionName: String,
        id: String
    ): DocMap? = try {
        database
            .getCollection(collectionName)!!
            .getDocument(id)!!
            .toMap()
    } catch (e: Exception) {
        logError("Error getting doc in $collectionName by id $id: ${e.message}")
        null
    }

    override fun getByParam(
        collectionName: String,
        param: String,
        value: Any
    ): List<DocMap> = try {
        val collection = getAndCreateIfNotExist(collectionName)
        val query = QueryBuilder.select(SelectResult.all())
            .from(DataSource.collection(collection))
            .where(Expression.property(param).equalTo(Expression.value(value)))
        val result = query.execute()
        result.allResults().mapNotNull { it.getDictionary(collectionName)?.toMap() }
    } catch (e: Exception) {
        logError("Error getting by param in $collectionName: ${e.message}")
        emptyList()
    }

    /**
     * Upserts:
     */

    override fun upsert(
        collectionName: String,
        document: DocMap,
        id: String
    ): Boolean = try {
        val collection = getAndCreateIfNotExist(collectionName)
        collection.save(MutableDocument(id, document))
        true
    } catch (e: Exception) {
        logError("Error upserting in collection $collectionName: ${e.message}")
        false
    }

    override fun upsertAll(
        collectionName: String,
        documents: List<DocMap>,
        ids: List<String>
    ) = try {
        val collection = getAndCreateIfNotExist(collectionName)
        documents.forEachIndexed { index, document ->
            collection.save(MutableDocument(ids[index], document))
        }
        true
    } catch (e: Exception) {
        logError("Error upserting all in collection $collectionName: ${e.message}")
        false
    }

    /**
     * Deletes:
     */

    override fun delete(
        collectionName: String,
        id: String
    ): Boolean = try {
        val collection = getAndCreateIfNotExist(collectionName)
        collection.purge(id)
        true
    } catch (e: Exception) {
        logError("Error deleting in collection $collectionName: ${e.message}")
        false
    }

    override fun deleteAll(
        collectionName: String,
        ids: List<String>
    ): Boolean {
        return try {
            val collection = getAndCreateIfNotExist(collectionName)
            ids.forEach(collection::purge)
            true
        } catch (e: Exception) {
            logError("Error deleting all in collection $collectionName: ${e.message}")
            false
        }
    }

    override fun deleteAll(
        collectionName: String
    ): Boolean {
        return try {
            val collection = getAndCreateIfNotExist(collectionName)
            val query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.collection(collection))
            query.execute().allResults().forEach { result ->
                (result.toMap()["uniqueId"] as? String)?.let {
                    collection.purge(it)
                }
            }
            true
        } catch (e: Exception) {
            logError("Error deleting all in collection $collectionName: ${e.message}")
            false
        }
    }

    override fun deleteByParam(
        collectionName: String,
        param: String,
        value: Any
    ): Boolean {
        return try {
            val collection = getAndCreateIfNotExist(collectionName)
            val query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.collection(collection))
                .where(Expression.property(param).equalTo(Expression.value(value)))
            query.execute().allResults().forEach { result ->
                (result.toMap()["uniqueId"] as? String)?.let {
                    collection.purge(it)
                }
            }
            true
        } catch (e: Exception) {
            logError("Error deleting all in collection $collectionName: ${e.message}")
            false
        }
    }

    /**
     * Register Listeners:
     */

    override fun registerCollectionChangeListener(
        collectionName: String,
        listener: (documents: List<DocMap>) -> Unit,
        retrieveOnlyChanges: Boolean
    ) = try {
        val collection = database.getCollection(collectionName)
        collection?.let {
            val token = it.addChangeListener { changes ->
                if (retrieveOnlyChanges)
                    listener(changes.documentIDs.mapNotNull { docIds ->
                        it.getDocument(docIds)?.toMap()
                    })
                else listener(
                    collection.indexes.mapNotNull { docId ->
                        collection.getDocument(docId)?.toMap()
                    }
                )
            }
            collectionsTokens[collectionName] = token
        } ?: run {
            logError("Error registering collection change listener in collection $collectionName: Collection not found")
        }
    } catch (e: Exception) {
        logError("Error registering collection change listener in collection $collectionName: ${e.message}")
    }

    override fun registerDocumentChangeListener(
        collectionName: String,
        documentId: String,
        listener: (changedDocument: DocMap) -> Unit
    ) = try {
        database.getCollection(collectionName)?.let { collection ->
            val token = collection.addDocumentChangeListener(documentId) { change ->
                collection.getDocument(change.documentID)?.let { doc ->
                    listener(doc.toMap())
                } ?: run {
                    logError("Error registering document change listener in collection $collectionName: Document not found")
                }
            }
            documentsTokens["$collectionName-$documentId"] = token
        } ?: run {
            logError("Error registering document change listener in collection $collectionName: Collection not found")
        }
    } catch (e: Exception) {
        logError("Error registering document change listener in collection $collectionName: ${e.message}")
    }

    /**
     * Unregister Listeners:
     */

    override fun unregisterCollectionChangeListener(
        collectionName: String
    ) {
        try {
            collectionsTokens[collectionName]?.let { token ->
                token.remove()
                collectionsTokens.remove(collectionName)
            } ?: run {
                logError("Error unregistering collection change listener in collection $collectionName: Listener not found")
            }
        } catch (e: Exception) {
            logError("Error unregistering collection change listener in collection $collectionName: ${e.message}")
        }
    }

    override fun unregisterDocumentChangeListener(
        collectionName: String,
        documentId: String
    ) {
        try {
            documentsTokens["$collectionName-$documentId"]?.let { token ->
                token.remove()
                documentsTokens.remove("$collectionName-$documentId")
            } ?: run {
                logError("Error unregistering document change listener in collection $collectionName: Listener not found")
            }
        } catch (e: Exception) {
            logError("Error unregistering document change listener in collection $collectionName: ${e.message}")
        }
    }

    //Privates:

    /**
     * Logs an error message and sends it to the log event data source.
     *
     * @param message [String] The error message to log.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    private fun logError(message: String) {
        logE(message)
        logEventDataSource.logError(this, message)
    }

    /**
     * Gets or creates a collection if it does not exist.
     *
     * @param collectionName [String] The name of the collection to retrieve or create.
     *
     * @return [Collection]? The collection if it exists or was created, null otherwise.
     *
     * @since 0.1.0
     * @author Sebas1705 24/07/2025
     */
    private fun getAndCreateIfNotExist(
        collectionName: String
    ): Collection =
        database.getCollection(collectionName) ?: database.createCollection(collectionName)
}