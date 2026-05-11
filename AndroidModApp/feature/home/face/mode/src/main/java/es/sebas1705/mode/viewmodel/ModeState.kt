package es.sebas1705.mode.viewmodel

import es.sebas1705.common.mvi.MVIBaseState

data class ModeState(
    val temp: String = "",
) : MVIBaseState

