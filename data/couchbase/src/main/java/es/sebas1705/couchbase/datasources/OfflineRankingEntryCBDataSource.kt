package es.sebas1705.couchbase.datasources

import es.sebas1705.couchbase.datasources.abstracts.CBDataSource
import es.sebas1705.couchbase.documents.OfflineRankingDoc
import es.sebas1705.couchbase.manager.ICouchbaseManager
import java.util.Locale
import javax.inject.Inject

class OfflineRankingEntryCBDataSource @Inject constructor(
    couchbaseManager: ICouchbaseManager
): CBDataSource<OfflineRankingDoc>(couchbaseManager, OfflineRankingDoc::class) {

    fun getByPlayerName(playerName: String): OfflineRankingDoc? =
        this.getFirstByParam(OfflineRankingDoc::playerName, playerName)

    fun idFromPlayerName(playerName: String): String {
        val normalized = playerName.trim().lowercase(Locale.ROOT)
        return "offline-ranking-$normalized"
    }

}
