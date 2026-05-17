package es.sebas1705.onlinegame.viewmodel

import es.sebas1705.common.mvi.MVIBaseIntent
import es.sebas1705.models.GameRoom
import es.sebas1705.models.NetworkMode

sealed interface OnlineGameIntent : MVIBaseIntent {

    /** Player selected a network mode (Local / Internet). */
    data class SelectMode(val mode: NetworkMode) : OnlineGameIntent

    /** Host: create and publish a room. */
    data class CreateRoom(val playerName: String, val maxPlayers: Int) : OnlineGameIntent

    /** Client: join an existing room from the lobby list. */
    data class JoinRoom(val room: GameRoom, val playerName: String) : OnlineGameIntent

    /** Host only: start the game when enough players are in the lobby. */
    data object StartGame : OnlineGameIntent

    /** In-game: current player has seen their card. */
    data object MarkRevealDone : OnlineGameIntent

    /** In-game: move to next player's card. */
    data object NextRevealPlayer : OnlineGameIntent

    /** In-game: vote to eliminate a player. */
    data class VotePlayer(val playerIndex: Int) : OnlineGameIntent

    /** In-game: impostor tries to guess the word. */
    data class SubmitGuess(val value: String) : OnlineGameIntent

    /** Leave / disconnect from the current session. */
    data object Disconnect : OnlineGameIntent
}
