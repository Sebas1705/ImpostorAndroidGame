package es.sebas1705.repositories.interfaces

import es.sebas1705.couchbase.documents.OfflineRankingDoc

interface IOfflineRankingRepository {

    suspend fun getAll(): List<OfflineRankingDoc>

    suspend fun incrementCivilianWins(playerName: String): Boolean

    suspend fun incrementImpostorWins(playerName: String): Boolean

    suspend fun resetCurrentStreak(playerName: String): Boolean
}
