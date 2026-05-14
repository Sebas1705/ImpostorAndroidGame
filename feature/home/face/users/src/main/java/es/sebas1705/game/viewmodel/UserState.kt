package es.sebas1705.game.viewmodel

import es.sebas1705.common.mvi.MVIBaseState

data class UserState(
    val temp: String = "",
) : MVIBaseState

