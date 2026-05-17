package es.sebas1705.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface PlayerAction {

    @Serializable
    @SerialName("vote")
    data class VotePlayer(val playerIndex: Int) : PlayerAction

    @Serializable
    @SerialName("guess")
    data class SubmitGuess(val value: String) : PlayerAction

    @Serializable
    @SerialName("reveal_done")
    data object MarkRevealDone : PlayerAction
}
