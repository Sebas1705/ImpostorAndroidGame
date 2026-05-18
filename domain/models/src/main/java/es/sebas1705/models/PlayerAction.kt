package es.sebas1705.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface PlayerAction {

    /** Player confirmed they saw their reveal card. */
    @Serializable
    @SerialName("reveal_done")
    data object MarkRevealDone : PlayerAction

    /** Vote to eliminate a player during discussion. */
    @Serializable
    @SerialName("vote")
    data class VotePlayer(val playerIndex: Int) : PlayerAction

    /** Impostor guesses the word. */
    @Serializable
    @SerialName("guess")
    data class SubmitGuess(val value: String) : PlayerAction

    /** Send a chat message during discussion. */
    @Serializable
    @SerialName("chat")
    data class SendChatMessage(val content: String) : PlayerAction

    /** Player accepts the result and is ready to return to the room. */
    @Serializable
    @SerialName("accept_result")
    data object AcceptResult : PlayerAction
}
