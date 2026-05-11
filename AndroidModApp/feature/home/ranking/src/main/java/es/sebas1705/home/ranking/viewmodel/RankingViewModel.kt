package es.sebas1705.home.ranking.viewmodel

import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.common.mvi.MVIBaseViewModel
import es.sebas1705.game.ReadOfflineRankingUseCase
import es.sebas1705.home.ranking.models.OfflineRankingRowUi
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val readOfflineRankingUseCase: ReadOfflineRankingUseCase
) : MVIBaseViewModel<RankingState, RankingIntent>(context) {

    override fun initState(): RankingState = RankingState()

    override fun intentHandler(intent: RankingIntent) {
        when (intent) {
            RankingIntent.Load -> loadOfflineRanking()
            is RankingIntent.SelectTab -> updateUi { it.copy(selectedTab = intent.tab) }
        }
    }

    private fun loadOfflineRanking() = execute(Dispatchers.IO) {
        updateUi { it.copy(isLoading = true, errorMessage = null) }

        runCatching { readOfflineRankingUseCase() }
            .onSuccess { entries ->
                updateUi {
                    it.copy(
                        isLoading = false,
                        offlineRows = entries.mapIndexed { index, entry ->
                            OfflineRankingRowUi(
                                position = index + 1,
                                playerName = entry.playerName,
                                civilianWins = entry.civilianWins,
                                impostorWins = entry.impostorWins,
                                totalWins = entry.totalWins
                            )
                        }.toImmutableList()
                    )
                }
            }
            .onFailure { throwable ->
                updateUi {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message
                    )
                }
            }
    }
}

