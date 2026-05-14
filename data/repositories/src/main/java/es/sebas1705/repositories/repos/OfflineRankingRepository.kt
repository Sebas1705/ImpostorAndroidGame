package es.sebas1705.repositories.repos

import es.sebas1705.couchbase.datasources.OfflineRankingEntryCBDataSource
import es.sebas1705.couchbase.documents.OfflineRankingDoc
import es.sebas1705.repositories.interfaces.IOfflineRankingRepository
import javax.inject.Inject

class OfflineRankingRepository @Inject constructor(
    private val offlineRankingEntryCBDataSource: OfflineRankingEntryCBDataSource
) : IOfflineRankingRepository {

    override suspend fun getAll(): List<OfflineRankingDoc> =
        offlineRankingEntryCBDataSource.getAll()

    override suspend fun incrementCivilianWins(playerName: String): Boolean =
        increment(playerName = playerName, civilianDelta = 1, impostorDelta = 0)

    override suspend fun incrementImpostorWins(playerName: String): Boolean =
        increment(playerName = playerName, civilianDelta = 0, impostorDelta = 1)

    override suspend fun resetCurrentStreak(playerName: String): Boolean {
        val normalizedName = playerName.trim()
        if (normalizedName.isEmpty()) return false

        val current = offlineRankingEntryCBDataSource.getByPlayerName(normalizedName)
        val updated = OfflineRankingDoc(
            id = current?.id ?: offlineRankingEntryCBDataSource.idFromPlayerName(normalizedName),
            playerName = current?.playerName ?: normalizedName,
            civilianWins = current?.civilianWins ?: 0,
            impostorWins = current?.impostorWins ?: 0,
            currentStreak = 0,
            bestStreak = current?.bestStreak ?: 0
        )

        return offlineRankingEntryCBDataSource.upsert(updated)
    }

    private fun increment(
        playerName: String,
        civilianDelta: Int,
        impostorDelta: Int
    ): Boolean {
        val normalizedName = playerName.trim()
        if (normalizedName.isEmpty()) return false

        val current = offlineRankingEntryCBDataSource.getByPlayerName(normalizedName)
        val newStreak = (current?.currentStreak ?: 0) + 1
        val updated = OfflineRankingDoc(
            id = current?.id ?: offlineRankingEntryCBDataSource.idFromPlayerName(normalizedName),
            playerName = current?.playerName ?: normalizedName,
            civilianWins = (current?.civilianWins ?: 0) + civilianDelta,
            impostorWins = (current?.impostorWins ?: 0) + impostorDelta,
            currentStreak = newStreak,
            bestStreak = maxOf(current?.bestStreak ?: 0, newStreak)
        )

        return offlineRankingEntryCBDataSource.upsert(updated)
    }
}
