package es.sebas1705.repositories.online

import es.sebas1705.firestore.lobby.GameRoomFirestoreDataSource
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
 *
 * Firebase usage is minimised: the Firestore room document and the RTDB game node
 * are both removed as soon as the game ends or the host disconnects.
 */
class FirebaseOnlineTransport(
    override val localPlayer: OnlinePlayer,
    private val lobbySource: GameRoomFirestoreDataSource,
    private val rtdbSource: RtdbGameDataSource,
) : IOnlineGameTransport, IOnlineLobbyRepository {

    private var currentRoomId: String? = null

    // ── IOnlineLobbyRepository ─────────────────────────────────────────────

    override fun observeRooms(): Flow<List<GameRoom>> = lobbySource.observeRooms()

    override suspend fun createRoom(hostName: String, maxPlayers: Int): Result<GameRoom> {
        val result = lobbySource.createRoom(hostName, maxPlayers)
        result.onSuccess { room ->
            currentRoomId = room.id
            rtdbSource.registerOnDisconnectCleanup(room.id)
        }
        return result
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

    override fun observeConnectedPlayers(): Flow<List<OnlinePlayer>> =
        // For internet mode, player list is managed via game state broadcasts.
        emptyFlow()

    override suspend fun sendStateTo(playerId: String, state: OnlineGameState): Result<Unit> {
        val roomId = currentRoomId ?: return Result.failure(IllegalStateException("No active room"))
        return rtdbSource.writePlayerState(roomId, playerId, state)
    }

    override suspend fun sendAction(action: PlayerAction): Result<Unit> {
        val roomId = currentRoomId ?: return Result.failure(IllegalStateException("Not in a room"))
        return rtdbSource.pushAction(roomId, localPlayer.id, action)
    }

    override suspend fun disconnect() {
        currentRoomId?.let {
            if (localPlayer.isHost) {
                lobbySource.deleteRoom(it)
                rtdbSource.deleteRoom(it)
            } else {
                lobbySource.updatePlayerCount(it, -1)
            }
        }
        currentRoomId = null
    }

    fun setRoomId(roomId: String) {
        currentRoomId = roomId
    }
}
