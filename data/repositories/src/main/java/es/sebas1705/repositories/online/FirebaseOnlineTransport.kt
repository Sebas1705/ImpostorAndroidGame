package es.sebas1705.repositories.online

import es.sebas1705.firestore.lobby.GameRoomFirestoreDataSource
import es.sebas1705.firestore.usage.InternetUsageDataSource
import es.sebas1705.models.GameRoom
import es.sebas1705.models.OnlineGameState
import es.sebas1705.models.OnlinePlayer
import es.sebas1705.models.PlayerAction
import es.sebas1705.realtime.game.RtdbGameDataSource
import es.sebas1705.network.interfaces.IOnlineGameTransport
import es.sebas1705.network.interfaces.IOnlineLobbyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Firebase-backed implementation of both the lobby and the game transport.
 *
 * Lobby  → Firestore `game_rooms` collection (tiny reads/writes, deleted on game end).
 * Game   → RTDB `games/{roomId}` (personalized state per player, deleted on game end).
 * Usage  → Firestore `config/internet_usage` (atomic daily session counter, guards free-tier).
 *
 * Firebase usage is minimised: the Firestore room document and the RTDB game node
 * are both removed as soon as the game ends or the host disconnects.
 */
class FirebaseOnlineTransport(
    override val localPlayer: OnlinePlayer,
    private val lobbySource: GameRoomFirestoreDataSource,
    private val rtdbSource: RtdbGameDataSource,
    private val usageSource: InternetUsageDataSource,
) : IOnlineGameTransport, IOnlineLobbyRepository {

    private var currentRoomId: String? = null

    // ── IOnlineLobbyRepository ─────────────────────────────────────────────

    override fun observeRooms(): Flow<List<GameRoom>> = lobbySource.observeRooms()

    override suspend fun createRoom(hostName: String, maxPlayers: Int): Result<GameRoom> {
        // Guard: atomically claim one daily Internet session slot before touching any
        // other Firebase resource. Returns a user-readable failure when the cap is reached.
        usageSource.tryClaimSession().getOrElse { return Result.failure(it) }

        val result = lobbySource.createRoom(hostName, maxPlayers)
        result.onSuccess { room ->
            currentRoomId = room.id
            rtdbSource.registerOnDisconnectCleanup(room.id)
            rtdbSource.writeRoomMember(room.id, localPlayer)
            rtdbSource.registerRoomHostDisconnectCleanup(room.id)
        }
        return result
    }

    override suspend fun joinRoom(room: GameRoom): Result<Unit> {
        currentRoomId = room.id
        rtdbSource.writeRoomMember(room.id, localPlayer)
            .onFailure { return Result.failure(it) }
        rtdbSource.registerRoomMemberDisconnectCleanup(room.id, localPlayer.id)
        lobbySource.updatePlayerCount(room.id, +1)
        return Result.success(Unit)
    }

    override suspend fun deleteRoom(roomId: String): Result<Unit> {
        lobbySource.deleteRoom(roomId)
        return rtdbSource.deleteRoom(roomId)
    }

    // ── IOnlineGameTransport ───────────────────────────────────────────────

    override fun observeGameState(): Flow<OnlineGameState> {
        val roomId = currentRoomId ?: return emptyFlow()
        return rtdbSource.observePlayerState(roomId, localPlayer.id)
    }

    override fun observePlayerActions(): Flow<Pair<String, PlayerAction>> {
        val roomId = currentRoomId ?: return emptyFlow()
        return rtdbSource.observeActions(roomId)
    }

    override fun observeConnectedPlayers(): Flow<List<OnlinePlayer>> {
        val roomId = currentRoomId ?: return emptyFlow()
        return rtdbSource.observeRoomMembers(roomId)
    }

    override suspend fun sendStateTo(playerId: String, state: OnlineGameState): Result<Unit> {
        val roomId = currentRoomId ?: return Result.failure(IllegalStateException("No active room"))
        return rtdbSource.writePlayerState(roomId, playerId, state)
    }

    override suspend fun sendAction(action: PlayerAction): Result<Unit> {
        val roomId = currentRoomId ?: return Result.failure(IllegalStateException("Not in a room"))
        return rtdbSource.pushAction(roomId, localPlayer.id, action)
    }

    override suspend fun disconnect() {
        currentRoomId?.let { roomId ->
            if (localPlayer.isHost) {
                lobbySource.deleteRoom(roomId)
                rtdbSource.deleteRoom(roomId)
                rtdbSource.deleteRoomNode(roomId)
            } else {
                rtdbSource.removeRoomMember(roomId, localPlayer.id)
                lobbySource.updatePlayerCount(roomId, -1)
            }
        }
        currentRoomId = null
    }

    fun setRoomId(roomId: String) {
        currentRoomId = roomId
    }
}
