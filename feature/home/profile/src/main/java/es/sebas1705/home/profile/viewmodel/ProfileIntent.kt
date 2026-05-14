package es.sebas1705.home.profile.viewmodel

import es.sebas1705.common.mvi.MVIBaseIntent

sealed interface ProfileIntent : MVIBaseIntent {
    data object Load : ProfileIntent
    data object SignOut : ProfileIntent
    data object ConsumeSignOutNavigation : ProfileIntent
    data class SelectTab(val tab: ProfileTab) : ProfileIntent
    data class ToggleOfflineRecordSort(val column: ProfileOfflineRecordSortColumn) : ProfileIntent
}

