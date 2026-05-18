package es.sebas1705.network.interfaces

import es.sebas1705.models.GameRoom
import kotlinx.coroutines.flow.Flow

interface IOnlineLobbyRepository {
    fun observeRooms(): Flow<List<GameRoom>>
    suspend fun createRoom(hostName: String, maxPlayers: Int): Result<GameRoom>
    suspend fun deleteRoom(roomId: String): Result<Unit>

    /**
     * Joins an existing room and registers the local player's presence.
     * For Firebase: writes to RTDB members node and registers onDisconnect cleanup.
     * For Local: establishes TCP connection to the host.
     */
    suspend fun joinRoom(room: GameRoom): Result<Unit>
}
