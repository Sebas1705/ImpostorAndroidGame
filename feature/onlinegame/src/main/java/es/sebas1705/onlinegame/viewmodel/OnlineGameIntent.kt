package es.sebas1705.onlinegame.viewmodel

import es.sebas1705.common.mvi.MVIBaseIntent
import es.sebas1705.models.Categories
import es.sebas1705.models.GameRoom
import es.sebas1705.models.Modes
import es.sebas1705.models.NetworkMode

sealed interface OnlineGameIntent : MVIBaseIntent {

    /** Called once when the screen opens. Sets up lobby observation. */
    data class Initialize(
        val categories: Set<Categories>,
        val mode: Modes,
        val impostors: Int,
        val networkMode: NetworkMode,
    ) : OnlineGameIntent

    /** Host: create and publish a new room. */
    data class CreateRoom(val playerName: String, val maxPlayers: Int) : OnlineGameIntent

    /** Client: join an existing room. */
    data class JoinRoom(val room: GameRoom, val playerName: String) : OnlineGameIntent

    /** Host only: update game config from the waiting room. */
    data class UpdateGameConfig(
        val categories: Set<Categories>,
        val mode: Modes,
        val impostors: Int,
        val discussionTimerSeconds: Int,
        val impostorsKnowEachOther: Boolean,
        val showNumOfImpostors: Boolean,
    ) : OnlineGameIntent

    /** Host only: start the game when ready. */
    data object StartGame : OnlineGameIntent

    /** Every player: confirm they saw their reveal card. */
    data object MarkRevealDone : OnlineGameIntent

    /** Any player: send a chat message during discussion. */
    data class SendChatMessage(val content: String) : OnlineGameIntent

    /** Host only: vote to eliminate a player. */
    data class VotePlayer(val playerIndex: Int) : OnlineGameIntent

    /** Impostor: guess the word. */
    data class SubmitGuess(val value: String) : OnlineGameIntent

    /** Every player: accept the result and return to the room. */
    data object AcceptResult : OnlineGameIntent

    /** Leave / disconnect from the current session. */
    data object Disconnect : OnlineGameIntent
}
