package es.sebas1705.network.interfaces

import es.sebas1705.models.OnlineGameState
import es.sebas1705.models.OnlinePlayer
import es.sebas1705.models.PlayerAction
import kotlinx.coroutines.flow.Flow

interface IOnlineGameTransport {

    val localPlayer: OnlinePlayer

    /** Emits the latest game state as received from the host (client side). */
    fun observeGameState(): Flow<OnlineGameState>

    /** Emits actions sent by clients as (senderId, action) pairs (host side). */
    fun observePlayerActions(): Flow<Pair<String, PlayerAction>>

    /** Emits the current list of connected players. */
    fun observeConnectedPlayers(): Flow<List<OnlinePlayer>>

    /** Host: broadcast a personalized state to a specific client. */
    suspend fun sendStateTo(playerId: String, state: OnlineGameState): Result<Unit>

    /** Client: send an action to the host. */
    suspend fun sendAction(action: PlayerAction): Result<Unit>

    suspend fun disconnect()
}
