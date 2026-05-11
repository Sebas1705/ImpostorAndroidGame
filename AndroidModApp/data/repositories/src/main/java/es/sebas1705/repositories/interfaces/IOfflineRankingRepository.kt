package es.sebas1705.repositories.interfaces

import es.sebas1705.couchbase.documents.OfflineRankingEntryDoc

interface IOfflineRankingRepository {

    suspend fun getAll(): List<OfflineRankingEntryDoc>

    suspend fun incrementCivilianWins(playerName: String): Boolean

    suspend fun incrementImpostorWins(playerName: String): Boolean
}
