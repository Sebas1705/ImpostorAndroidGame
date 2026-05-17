package es.sebas1705.realtime.game

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.sebas1705.models.OnlineGameState
import es.sebas1705.models.OnlinePlayer
import es.sebas1705.models.PlayerAction
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject

/**
 * Raw Firebase RTDB operations for online game state.
 *
 * Data layout:
 *   games/{roomId}/players/{playerId}/state  → JSON OnlineGameState (host writes, client reads)
 *   games/{roomId}/actions/{uuid}            → JSON PlayerActionEnvelope (client writes, host reads)
 */
class RtdbGameDataSource @Inject constructor(
    private val database: FirebaseDatabase,
) {
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private fun gameRef(roomId: String) = database.getReference("games/$roomId")
    private fun playerStateRef(roomId: String, playerId: String) =
        gameRef(roomId).child("players/$playerId/state")
    private fun actionsRef(roomId: String) = gameRef(roomId).child("actions")

    // ── Host: write personalized state for a player ────────────────────────

    suspend fun writePlayerState(
        roomId: String,
        playerId: String,
        state: OnlineGameState,
    ): Result<Unit> = runCatching {
        playerStateRef(roomId, playerId)
            .setValue(json.encodeToString(state))
            .await()
    }

    // ── Client: observe own state updates ─────────────────────────────────

    fun observePlayerState(roomId: String, playerId: String): Flow<OnlineGameState> =
        callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val raw = snapshot.getValue(String::class.java) ?: return
                    runCatching { json.decodeFromString<OnlineGameState>(raw) }
                        .onSuccess { trySend(it) }
                }
                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }
            val ref = playerStateRef(roomId, playerId)
            ref.addValueEventListener(listener)
            awaitClose { ref.removeEventListener(listener) }
        }

    // ── Client: send an action ─────────────────────────────────────────────

    suspend fun pushAction(
        roomId: String,
        senderId: String,
        action: PlayerAction,
    ): Result<Unit> = runCatching {
        val envelope = mapOf(
            "senderId" to senderId,
            "action" to json.encodeToString(action),
        )
        actionsRef(roomId).child(UUID.randomUUID().toString()).setValue(envelope).await()
    }

    // ── Host: observe incoming actions ────────────────────────────────────

    fun observeActions(roomId: String): Flow<Pair<String, PlayerAction>> =
        callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { child ->
                        val senderId = child.child("senderId").getValue(String::class.java) ?: return@forEach
                        val actionJson = child.child("action").getValue(String::class.java) ?: return@forEach
                        runCatching { json.decodeFromString<PlayerAction>(actionJson) }
                            .onSuccess {
                                trySend(senderId to it)
                                child.ref.removeValue()
                            }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }
            val ref = actionsRef(roomId)
            ref.addValueEventListener(listener)
            awaitClose { ref.removeEventListener(listener) }
        }

    // ── Cleanup ────────────────────────────────────────────────────────────

    suspend fun deleteRoom(roomId: String): Result<Unit> =
        runCatching { gameRef(roomId).removeValue().await() }

    fun registerOnDisconnectCleanup(roomId: String) {
        gameRef(roomId).onDisconnect().removeValue()
    }
}
