package es.sebas1705.main.viewmodel

import es.sebas1705.common.mvi.MVIBaseIntent

sealed interface MainIntent : MVIBaseIntent {
    /** Loads initial app data (words, auth state) from the splash screen. */
    data object ChargeData : MainIntent
}
