package es.sebas1705.game

import es.sebas1705.models.OfflineRankingEntry
import es.sebas1705.repositories.interfaces.IOfflineRankingRepository
import javax.inject.Inject

class ReadOfflineRankingUseCase @Inject constructor(
    private val offlineRankingRepository: IOfflineRankingRepository
) {
    suspend operator fun invoke(): List<OfflineRankingEntry> =
        offlineRankingRepository.getAll()
            .map { doc ->
                OfflineRankingEntry(
                    playerName = doc.playerName,
                    civilianWins = doc.civilianWins,
                    impostorWins = doc.impostorWins
                )
            }
            .sortedWith(compareByDescending<OfflineRankingEntry> { it.totalWins }.thenBy { it.playerName })
}
