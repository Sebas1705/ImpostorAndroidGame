package es.sebas1705.network.tcp

import es.sebas1705.models.OnlinePlayer
import es.sebas1705.models.PlayerAction
import es.sebas1705.network.messages.NetworkMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

/**
 * TCP server that runs on the host device.
 * Accepts client connections and relays messages.
 */
class LocalNetworkServer {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private val _connectedPlayers = MutableStateFlow<List<OnlinePlayer>>(emptyList())
    val connectedPlayers: StateFlow<List<OnlinePlayer>> = _connectedPlayers.asStateFlow()

    private val _incomingActions = MutableSharedFlow<Pair<String, PlayerAction>>(extraBufferCapacity = 64)
    val incomingActions: SharedFlow<Pair<String, PlayerAction>> = _incomingActions.asSharedFlow()

    private val clientWriters = mutableMapOf<String, PrintWriter>()
    private var serverSocket: ServerSocket? = null

    val port: Int get() = serverSocket?.localPort ?: 0

    fun start(hostPlayer: OnlinePlayer) {
        // Host is already "in the room" — seed the list so observers see them immediately.
        _connectedPlayers.value = listOf(hostPlayer)
        serverSocket = ServerSocket(0)
        scope.launch {
            while (isActive) {
                val clientSocket = runCatching { serverSocket?.accept() }.getOrNull() ?: break
                launch { handleClient(clientSocket, hostPlayer) }
            }
        }
    }

    private suspend fun handleClient(socket: Socket, hostPlayer: OnlinePlayer) {
        val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        val writer = PrintWriter(socket.getOutputStream(), true)
        var clientId: String? = null

        try {
            // First message from client must be a PlayerJoined with their info.
            val firstLine = reader.readLine() ?: return
            val firstMsg = runCatching { json.decodeFromString<NetworkMessage>(firstLine) }.getOrNull()

            if (firstMsg is NetworkMessage.PlayerJoined) {
                val player = firstMsg.player
                clientId = player.id
                clientWriters[clientId] = writer

                _connectedPlayers.update { it + player }

                // Notify all clients about the new player.
                broadcast(NetworkMessage.PlayerJoined(player))
                // Send host info to the new client as well.
                send(clientId, NetworkMessage.PlayerJoined(hostPlayer))
            } else {
                socket.close()
                return
            }

            while (scope.isActive) {
                val line = reader.readLine() ?: break
                val msg = runCatching { json.decodeFromString<NetworkMessage>(line) }.getOrNull()
                when (msg) {
                    is NetworkMessage.PlayerActionMessage ->
                        _incomingActions.emit(msg.senderId to msg.action)
                    is NetworkMessage.Ping -> send(clientId!!, NetworkMessage.Pong)
                    else -> Unit
                }
            }
        } finally {
            clientId?.let { id ->
                clientWriters.remove(id)
                _connectedPlayers.update { list -> list.filter { it.id != id } }
                broadcast(NetworkMessage.PlayerLeft(id))
            }
            runCatching { socket.close() }
        }
    }

    fun send(playerId: String, message: NetworkMessage) {
        val writer = clientWriters[playerId] ?: return
        runCatching { writer.println(json.encodeToString(message)) }
    }

    fun broadcast(message: NetworkMessage) {
        val encoded = json.encodeToString(message)
        clientWriters.values.forEach { runCatching { it.println(encoded) } }
    }

    fun stop() {
        scope.cancel()
        runCatching { serverSocket?.close() }
        clientWriters.values.forEach { runCatching { it.close() } }
        clientWriters.clear()
    }
}
