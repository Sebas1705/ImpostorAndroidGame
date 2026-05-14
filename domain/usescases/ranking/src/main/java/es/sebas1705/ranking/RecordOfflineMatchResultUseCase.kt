package es.sebas1705.ranking

import es.sebas1705.repositories.interfaces.IOfflineRankingRepository
import javax.inject.Inject

class RecordOfflineMatchResultUseCase @Inject constructor(
    private val offlineRankingRepository: IOfflineRankingRepository
) {
    suspend operator fun invoke(
        allPlayerNames: Set<String>,
        civilianWinnerNames: Set<String>,
        impostorWinnerNames: Set<String>
    ) {
        val normalizedAllPlayers = allPlayerNames
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toSet()
        val normalizedCivilianWinners = civilianWinnerNames
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toSet()
        val normalizedImpostorWinners = impostorWinnerNames
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toSet()

        normalizedCivilianWinners
            .forEach { offlineRankingRepository.incrementCivilianWins(it) }

        normalizedImpostorWinners
            .forEach { offlineRankingRepository.incrementImpostorWins(it) }

        val winners = normalizedCivilianWinners + normalizedImpostorWinners
        (normalizedAllPlayers - winners)
            .forEach { offlineRankingRepository.resetCurrentStreak(it) }
    }
}

