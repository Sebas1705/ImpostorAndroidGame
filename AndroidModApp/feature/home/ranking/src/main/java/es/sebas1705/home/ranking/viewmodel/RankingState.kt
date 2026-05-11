package es.sebas1705.home.ranking.viewmodel

import es.sebas1705.common.mvi.MVIBaseState
import es.sebas1705.home.ranking.models.OfflineRankingRowUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class RankingState(
    val selectedTab: RankingTab = RankingTab.Offline,
    val offlineRows: ImmutableList<OfflineRankingRowUi> = persistentListOf(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : MVIBaseState

enum class RankingTab {
    Offline,
    Online
}

