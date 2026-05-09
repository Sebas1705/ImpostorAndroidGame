package es.sebas1705.couchbase.manager

import es.sebas1705.couchbase.config.alias.DocMap

/**
 * Interface for managing Couchbase operations.
 *
 * This interface defines methods for selecting, upserting, deleting documents,
 * and registering/unregistering change listeners in a Couchbase database.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
interface ICouchbaseManager {

    /**
     * Selects:
     */

    /**
     * Gets all documents in a collection.
     *
     * @param collectionName [String] The name of the collection to retrieve documents from.
     *
     * @return [List]<[DocMap]> A list of documents as maps, or an empty list if an error occurs.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun getAll(
        collectionName: String,
    ): List<DocMap>

    /**
     * Gets a document by its ID from a collection.
     *
     * @param collectionName [String] The name of the collection to retrieve the document from.
     * @param id [String] The ID of the document to retrieve.
     *
     * @return [DocMap]? The document as a map if found, null otherwise.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun get(
        collectionName: String,
        id: String
    ): DocMap?

    /**
     * Gets documents by a specific parameter and value from a collection.
     *
     * @param collectionName [String] The name of the collection to retrieve documents from.
     * @param param [String] The parameter to filter documents by.
     * @param value [Any] The value of the parameter to filter documents by.
     *
     * @return [List]<[DocMap]> A list of documents as maps that match the parameter and value, or an empty list if an error occurs.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun getByParam(
        collectionName: String,
        param: String,
        value: Any
    ): List<DocMap>

    /**
     * Upserts:
     */

    /**
     * Inserts a document into a collection or replaces it if it already exists.
     *
     * @param collectionName [String] The name of the collection to insert or replace the document in.
     * @param document [DocMap] The document to insert or replace.
     * @param id [String] The ID of the document to insert or replace.
     *
     * @return [Boolean] True if the operation was successful, false otherwise.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun upsert(
        collectionName: String,
        document: DocMap,
        id: String
    ): Boolean

    /**
     * Inserts or replaces multiple documents in a collection.
     *
     * @param collectionName [String] The name of the collection to insert or replace documents in.
     * @param documents [List]<[DocMap]> The list of documents to insert or replace.
     * @param ids [List]<[String]> The list of IDs corresponding to the documents.
     *
     * @return [Boolean] True if all operations were successful, false otherwise.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun upsertAll(
        collectionName: String,
        documents: List<DocMap>,
        ids: List<String>
    ): Boolean

    /**
     * Deletes:
     */

    /**
     * Deletes a document by its ID from a collection.
     *
     * @param collectionName [String] The name of the collection to delete the document from.
     * @param id [String] The ID of the document to delete.
     *
     * @return [Boolean] True if the deletion was successful, false otherwise.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun delete(
        collectionName: String,
        id: String
    ): Boolean

    /**
     * Deletes multiple documents by their IDs from a collection.
     *
     * @param collectionName [String] The name of the collection to delete documents from.
     * @param ids [List]<[String]> The list of IDs of the documents to delete.
     *
     * @return [Boolean] True if all deletions were successful, false otherwise.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun deleteAll(
        collectionName: String,
        ids: List<String>
    ): Boolean

    /**
     * Deletes all Documents.
     *
     * @param collectionName [String] The name of the collection to delete documents from.
     *
     * @return [Boolean] True if all deletions were successful, false otherwise.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun deleteAll(
        collectionName: String
    ): Boolean

    /**
     * Deletes documents by a specific parameter and value from a collection.
     *
     * @param collectionName [String] The name of the collection to delete documents from.
     * @param param [String] The parameter to filter documents by.
     * @param value [Any] The value of the parameter to filter documents by.
     *
     * @return [Boolean] True if the deletion was successful, false otherwise.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun deleteByParam(
        collectionName: String,
        param: String,
        value: Any
    ): Boolean

    /**
     * Register Listeners:
     */

    /**
     * Registers a change listener for a collection.
     *
     * @param collectionName [String] The name of the collection to register the listener for.
     * @param listener [Function1]<[List]<[DocMap]>, [Unit]> The listener function to be called when changes occur.
     * @param retrieveOnlyChanges [Boolean] If true, only changes will be retrieved, otherwise all documents in the collection will be retrieved.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun registerCollectionChangeListener(
        collectionName: String,
        listener: (documents: List<DocMap>) -> Unit,
        retrieveOnlyChanges: Boolean = false
    )

    /**
     * Registers a change listener for a specific document in a collection.
     *
     * @param collectionName [String] The name of the collection to register the listener for.
     * @param documentId [String] The ID of the document to register the listener for.
     * @param listener [Function1]<[DocMap], [Unit]> The listener function to be called when the document changes.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun registerDocumentChangeListener(
        collectionName: String,
        documentId: String,
        listener: (changedDocument: DocMap) -> Unit
    )

    /**
     * Unregister Listeners:
     */

    /**
     * Unregisters a change listener for a collection.
     *
     * @param collectionName [String] The name of the collection to unregister the listener for.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun unregisterCollectionChangeListener(
        collectionName: String
    )

    /**
     * Unregisters a change listener for a specific document in a collection.
     *
     * @param collectionName [String] The name of the collection to unregister the listener for.
     * @param documentId [String] The ID of the document to unregister the listener for.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun unregisterDocumentChangeListener(
        collectionName: String,
        documentId: String
    )
}