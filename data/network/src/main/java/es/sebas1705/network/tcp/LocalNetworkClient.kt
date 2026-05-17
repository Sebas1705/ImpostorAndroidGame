package es.sebas1705.network.tcp

import es.sebas1705.models.OnlineGameState
import es.sebas1705.models.OnlinePlayer
import es.sebas1705.network.config.SettingsLAN
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
import java.net.Socket

/**
 * TCP client that runs on non-host devices.
 * Connects to the host's [LocalNetworkServer] and exchanges messages.
 */
class LocalNetworkClient {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private val _gameState = MutableSharedFlow<OnlineGameState>(replay = 1, extraBufferCapacity = 16)
    val gameState: SharedFlow<OnlineGameState> = _gameState.asSharedFlow()

    private val _connectedPlayers = MutableStateFlow<List<OnlinePlayer>>(emptyList())
    val connectedPlayers: StateFlow<List<OnlinePlayer>> = _connectedPlayers.asStateFlow()

    private var writer: PrintWriter? = null
    private var socket: Socket? = null

    fun connect(
        hostAddress: String,
        port: Int,
        localPlayer: OnlinePlayer,
    ) {
        scope.launch {
            socket = Socket().apply {
                soTimeout = SettingsLAN.CONNECT_TIMEOUT_MS
                connect(java.net.InetSocketAddress(hostAddress, port), SettingsLAN.CONNECT_TIMEOUT_MS)
            }
            val reader = BufferedReader(InputStreamReader(socket!!.getInputStream()))
            writer = PrintWriter(socket!!.getOutputStream(), true)

            // Announce ourselves.
            send(NetworkMessage.PlayerJoined(localPlayer))

            while (scope.isActive) {
                val line = reader.readLine() ?: break
                val msg = runCatching { json.decodeFromString<NetworkMessage>(line) }.getOrNull()
                when (msg) {
                    is NetworkMessage.GameStateUpdate -> _gameState.emit(msg.state)
                    is NetworkMessage.PlayerJoined ->
                        _connectedPlayers.update { list ->
                            if (list.any { it.id == msg.player.id }) list
                            else list + msg.player
                        }
                    is NetworkMessage.PlayerLeft ->
                        _connectedPlayers.update { list -> list.filter { it.id != msg.playerId } }
                    is NetworkMessage.Ping -> send(NetworkMessage.Pong)
                    else -> Unit
                }
            }
        }
    }

    fun send(message: NetworkMessage) {
        runCatching { writer?.println(json.encodeToString(message)) }
    }

    fun stop() {
        scope.cancel()
        runCatching { socket?.close() }
    }
}
