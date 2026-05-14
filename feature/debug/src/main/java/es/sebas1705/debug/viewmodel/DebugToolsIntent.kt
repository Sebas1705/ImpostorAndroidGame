package es.sebas1705.debug.viewmodel

import es.sebas1705.common.mvi.MVIBaseIntent

sealed interface DebugToolsIntent : MVIBaseIntent {
    data object RefreshAll : DebugToolsIntent
    data object ResetDefaultWords : DebugToolsIntent
    data class SelectTab(val tab: DebugMetricsTab) : DebugToolsIntent
}