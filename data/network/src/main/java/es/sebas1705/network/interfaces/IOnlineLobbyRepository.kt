package es.sebas1705.network.interfaces

import es.sebas1705.models.GameRoom
import kotlinx.coroutines.flow.Flow

interface IOnlineLobbyRepository {
    fun observeRooms(): Flow<List<GameRoom>>
    suspend fun createRoom(hostName: String, maxPlayers: Int): Result<GameRoom>
    suspend fun deleteRoom(roomId: String): Result<Unit>
}
