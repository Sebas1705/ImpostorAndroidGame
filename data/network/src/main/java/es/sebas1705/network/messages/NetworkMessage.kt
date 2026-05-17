package es.sebas1705.network.messages

import es.sebas1705.models.OnlineGameState
import es.sebas1705.models.OnlinePlayer
import es.sebas1705.models.PlayerAction
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface NetworkMessage {

    /** Host → specific client: their personalized game state. */
    @Serializable
    @SerialName("state")
    data class GameStateUpdate(val state: OnlineGameState) : NetworkMessage

    /** Client → host: a game action. */
    @Serializable
    @SerialName("action")
    data class PlayerActionMessage(
        val senderId: String,
        val action: PlayerAction,
    ) : NetworkMessage

    /** Host → all clients: someone joined. */
    @Serializable
    @SerialName("player_joined")
    data class PlayerJoined(val player: OnlinePlayer) : NetworkMessage

    /** Host → all clients: someone left. */
    @Serializable
    @SerialName("player_left")
    data class PlayerLeft(val playerId: String) : NetworkMessage

    /** Bidirectional keepalive. */
    @Serializable
    @SerialName("ping")
    data object Ping : NetworkMessage

    @Serializable
    @SerialName("pong")
    data object Pong : NetworkMessage

    @Serializable
    @SerialName("error")
    data class Error(val message: String) : NetworkMessage
}
