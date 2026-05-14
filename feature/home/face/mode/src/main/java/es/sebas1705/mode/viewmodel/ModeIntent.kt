package es.sebas1705.mode.viewmodel

import es.sebas1705.common.mvi.MVIBaseIntent
import es.sebas1705.models.Modes

sealed interface ModeIntent : MVIBaseIntent {
    data class Save(
        val mode: Modes,
        val impostors: Int,
        val showImpostorsInResult: Boolean
    ) : ModeIntent
}

