package es.sebas1705.game.viewmodel

import es.sebas1705.common.mvi.MVIBaseIntent

sealed interface UserIntent : MVIBaseIntent {

    data class Save(
        val playerNames: List<String>
    ) : UserIntent
}

