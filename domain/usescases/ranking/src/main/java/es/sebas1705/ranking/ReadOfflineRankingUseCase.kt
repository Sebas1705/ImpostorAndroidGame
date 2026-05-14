package es.sebas1705.ranking

import es.sebas1705.models.OfflineRankingModel
import es.sebas1705.repositories.interfaces.IOfflineRankingRepository
import javax.inject.Inject

class ReadOfflineRankingUseCase @Inject constructor(
    private val offlineRankingRepository: IOfflineRankingRepository
) {
    suspend operator fun invoke(): List<OfflineRankingModel> =
        offlineRankingRepository.getAll()
            .map { doc ->
                OfflineRankingModel(
                    playerName = doc.playerName,
                    civilianWins = doc.civilianWins,
                    impostorWins = doc.impostorWins,
                    currentStreak = doc.currentStreak ?: 0,
                    bestStreak = doc.bestStreak ?: 0
                )
            }
            .sortedWith(compareByDescending<OfflineRankingModel> { it.totalWins }.thenBy { it.playerName })
}

