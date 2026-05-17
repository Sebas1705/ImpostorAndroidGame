package es.sebas1705.firestore.lobby

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import es.sebas1705.models.GameRoom
import es.sebas1705.models.NetworkMode
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

/**
 * Manages the public game-room listing in Firestore.
 * Each room is a small document (~200 bytes) that is deleted when the game ends,
 * keeping Firestore usage minimal.
 */
class GameRoomFirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
) {

    private val rooms get() = firestore.collection(COLLECTION)

    fun observeRooms(): Flow<List<GameRoom>> = callbackFlow {
        val listener = rooms.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                trySend(emptyList())
                return@addSnapshotListener
            }
            val list = snapshot.documents.mapNotNull { doc ->
                runCatching {
                    GameRoom(
                        id = doc.getString(FIELD_ID) ?: return@mapNotNull null,
                        hostName = doc.getString(FIELD_HOST_NAME) ?: "",
                        playerCount = (doc.getLong(FIELD_PLAYER_COUNT) ?: 1L).toInt(),
                        maxPlayers = (doc.getLong(FIELD_MAX_PLAYERS) ?: 8L).toInt(),
                        networkMode = NetworkMode.Internet,
                    )
                }.getOrNull()
            }
            trySend(list)
        }
        awaitClose { listener.remove() }
    }

    suspend fun createRoom(hostName: String, maxPlayers: Int): Result<GameRoom> =
        runCatching {
            val id = UUID.randomUUID().toString().take(8)
            val data = mapOf(
                FIELD_ID to id,
                FIELD_HOST_NAME to hostName,
                FIELD_PLAYER_COUNT to 1L,
                FIELD_MAX_PLAYERS to maxPlayers.toLong(),
            )
            rooms.document(id).set(data, SetOptions.merge()).await()
            GameRoom(
                id = id,
                hostName = hostName,
                playerCount = 1,
                maxPlayers = maxPlayers,
                networkMode = NetworkMode.Internet,
            )
        }

    suspend fun updatePlayerCount(roomId: String, delta: Int): Result<Unit> =
        runCatching {
            val doc = rooms.document(roomId)
            firestore.runTransaction { tx ->
                val snap = tx.get(doc)
                val current = (snap.getLong(FIELD_PLAYER_COUNT) ?: 1L) + delta
                tx.update(doc, FIELD_PLAYER_COUNT, current.coerceAtLeast(0))
            }.await()
        }

    suspend fun deleteRoom(roomId: String): Result<Unit> =
        runCatching { rooms.document(roomId).delete().await() }

    companion object {
        private const val COLLECTION = "game_rooms"
        private const val FIELD_ID = "id"
        private const val FIELD_HOST_NAME = "hostName"
        private const val FIELD_PLAYER_COUNT = "playerCount"
        private const val FIELD_MAX_PLAYERS = "maxPlayers"
    }
}
