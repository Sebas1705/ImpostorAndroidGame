package es.sebas1705.game

import es.sebas1705.repositories.interfaces.IOfflineRankingRepository
import javax.inject.Inject

class RecordOfflineMatchResultUseCase @Inject constructor(
    private val offlineRankingRepository: IOfflineRankingRepository
) {
    suspend operator fun invoke(
        civilianWinnerNames: Set<String>,
        impostorWinnerNames: Set<String>
    ) {
        civilianWinnerNames
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .forEach { offlineRankingRepository.incrementCivilianWins(it) }

        impostorWinnerNames
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .forEach { offlineRankingRepository.incrementImpostorWins(it) }
    }
}

