package es.sebas1705.network.transport

import android.content.Context
import es.sebas1705.models.GameRoom
import es.sebas1705.models.NetworkMode
import es.sebas1705.models.OnlineGameState
import es.sebas1705.models.OnlinePlayer
import es.sebas1705.models.PlayerAction
import es.sebas1705.network.messages.NetworkMessage
import es.sebas1705.network.nsd.NsdHelper
import es.sebas1705.network.tcp.LocalNetworkClient
import es.sebas1705.network.tcp.LocalNetworkServer
import es.sebas1705.network.interfaces.IOnlineGameTransport
import es.sebas1705.network.interfaces.IOnlineLobbyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * Local-network implementation of both the lobby discovery and the game transport.
 * One instance covers the entire lifecycle of a local-network game session.
 */
class LocalNetworkTransport(
    context: Context,
    override val localPlayer: OnlinePlayer,
) : IOnlineGameTransport, IOnlineLobbyRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val nsdHelper = NsdHelper(context)

    private var server: LocalNetworkServer? = null
    private var client: LocalNetworkClient? = null
    val isHost: Boolean get() = server != null

    // ── IOnlineLobbyRepository ─────────────────────────────────────────────

    override fun observeRooms(): Flow<List<GameRoom>> = nsdHelper.discoverRooms()

    override suspend fun createRoom(hostName: String, maxPlayers: Int): Result<GameRoom> {
        val roomId = NsdHelper.generateRoomId()
        val srv = LocalNetworkServer().also { server = it }
        srv.start(localPlayer)
        val port = srv.port

        var registrationResult: Result<Unit> = Result.failure(RuntimeException("Timeout"))
        nsdHelper.registerService(
            hostName = hostName,
            port = port,
            maxPlayers = maxPlayers,
            roomId = roomId,
        ).collect { result ->
            registrationResult = result
            return@collect
        }

        return registrationResult.map {
            GameRoom(
                id = roomId,
                hostName = hostName,
                playerCount = 1,
                maxPlayers = maxPlayers,
                networkMode = NetworkMode.Local,
                hostAddress = "",
                port = port,
            )
        }
    }

    override suspend fun deleteRoom(roomId: String): Result<Unit> {
        server?.stop()
        server = null
        return Result.success(Unit)
    }

    fun joinRoom(room: GameRoom) {
        val cli = LocalNetworkClient().also { client = it }
        cli.connect(
            hostAddress = room.hostAddress,
            port = room.port,
            localPlayer = localPlayer,
        )
    }

    // ── IOnlineGameTransport ───────────────────────────────────────────────

    override fun observeGameState(): Flow<OnlineGameState> =
        client?.gameState ?: emptyFlow()

    override fun observePlayerActions(): Flow<Pair<String, PlayerAction>> =
        server?.incomingActions ?: emptyFlow()

    override fun observeConnectedPlayers(): Flow<List<OnlinePlayer>> =
        server?.connectedPlayers ?: client?.connectedPlayers ?: emptyFlow()

    override suspend fun sendStateTo(playerId: String, state: OnlineGameState): Result<Unit> =
        runCatching {
            server?.send(playerId, NetworkMessage.GameStateUpdate(state))
                ?: error("Not host")
        }

    override suspend fun sendAction(action: PlayerAction): Result<Unit> =
        runCatching {
            client?.send(
                NetworkMessage.PlayerActionMessage(
                    senderId = localPlayer.id,
                    action = action,
                )
            ) ?: error("Not connected as client")
        }

    override suspend fun disconnect() {
        server?.stop()
        client?.stop()
        server = null
        client = null
    }
}
