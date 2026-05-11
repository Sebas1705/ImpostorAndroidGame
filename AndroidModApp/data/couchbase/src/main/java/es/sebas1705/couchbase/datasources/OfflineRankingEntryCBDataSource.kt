package es.sebas1705.couchbase.datasources

import es.sebas1705.couchbase.documents.OfflineRankingEntryDoc
import es.sebas1705.couchbase.documents.abstracts.fromMap
import es.sebas1705.couchbase.manager.ICouchbaseManager
import java.util.Locale
import javax.inject.Inject

class OfflineRankingEntryCBDataSource @Inject constructor(
    private val couchbaseManager: ICouchbaseManager
) {

    fun getAll(): List<OfflineRankingEntryDoc> =
        couchbaseManager.getAll(COLLECTION_NAME).map { it.fromMap(OfflineRankingEntryDoc::class) }

    fun getByPlayerName(playerName: String): OfflineRankingEntryDoc? =
        couchbaseManager.get(COLLECTION_NAME, idFromPlayerName(playerName))
            ?.fromMap(OfflineRankingEntryDoc::class)

    fun upsert(entry: OfflineRankingEntryDoc): Boolean =
        couchbaseManager.upsert(COLLECTION_NAME, entry.asMap(), entry.id)

    fun idFromPlayerName(playerName: String): String =
        playerName.trim().lowercase(Locale.ROOT).replace("\\s+".toRegex(), "_")

    private companion object {
        const val COLLECTION_NAME = "offline_ranking_entry_collection"
    }
}

