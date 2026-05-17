package es.sebas1705.network.nsd

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import es.sebas1705.models.GameRoom
import es.sebas1705.models.NetworkMode
import es.sebas1705.network.config.SettingsLAN
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID

/**
 * Wraps Android NSD (Network Service Discovery) for registering and
 * discovering local game rooms over the same WiFi network.
 */
class NsdHelper(context: Context) {

    private val nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager

    // ── Host side ──────────────────────────────────────────────────────────

    fun registerService(
        hostName: String,
        port: Int,
        maxPlayers: Int,
        roomId: String,
    ): Flow<Result<Unit>> = callbackFlow {
        val serviceInfo = NsdServiceInfo().apply {
            serviceName = "${SettingsLAN.SERVICE_NAME_PREFIX}_$roomId"
            serviceType = SettingsLAN.SERVICE_TYPE
            this.port = port
            setAttribute("host", hostName)
            setAttribute("max", maxPlayers.toString())
            setAttribute("id", roomId)
        }

        val listener = object : NsdManager.RegistrationListener {
            override fun onServiceRegistered(info: NsdServiceInfo) {
                trySend(Result.success(Unit))
            }

            override fun onRegistrationFailed(info: NsdServiceInfo, errorCode: Int) {
                trySend(Result.failure(RuntimeException("NSD registration failed: $errorCode")))
                close()
            }

            override fun onServiceUnregistered(info: NsdServiceInfo) {
                close()
            }

            override fun onUnregistrationFailed(info: NsdServiceInfo, errorCode: Int) {}
        }

        nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, listener)

        awaitClose {
            runCatching { nsdManager.unregisterService(listener) }
        }
    }

    // ── Client side ────────────────────────────────────────────────────────

    fun discoverRooms(): Flow<List<GameRoom>> = callbackFlow {
        val rooms = mutableMapOf<String, GameRoom>()

        val resolveQueue = ArrayDeque<NsdServiceInfo>()
        var isResolving = false

        fun resolveNext() {
            val next = resolveQueue.removeFirstOrNull() ?: run { isResolving = false; return }
            isResolving = true
            nsdManager.resolveService(next, object : NsdManager.ResolveListener {
                override fun onServiceResolved(info: NsdServiceInfo) {
                    val id = info.attributes["id"]?.decodeToString() ?: return
                    val host = info.attributes["host"]?.decodeToString() ?: "Unknown"
                    val max = info.attributes["max"]?.decodeToString()?.toIntOrNull() ?: 8
                    val room = GameRoom(
                        id = id,
                        hostName = host,
                        playerCount = 1,
                        maxPlayers = max,
                        networkMode = NetworkMode.Local,
                        hostAddress = info.host?.hostAddress ?: "",
                        port = info.port,
                    )
                    rooms[id] = room
                    trySend(rooms.values.toList())
                    resolveNext()
                }

                override fun onResolveFailed(info: NsdServiceInfo, errorCode: Int) {
                    resolveNext()
                }
            })
        }

        val discoveryListener = object : NsdManager.DiscoveryListener {
            override fun onDiscoveryStarted(serviceType: String) {}

            override fun onServiceFound(info: NsdServiceInfo) {
                if (info.serviceType == SettingsLAN.SERVICE_TYPE) {
                    resolveQueue.add(info)
                    if (!isResolving) resolveNext()
                }
            }

            override fun onServiceLost(info: NsdServiceInfo) {
                val id = info.serviceName.removePrefix("${SettingsLAN.SERVICE_NAME_PREFIX}_")
                rooms.remove(id)
                trySend(rooms.values.toList())
            }

            override fun onDiscoveryStopped(serviceType: String) {
                close()
            }

            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                close(RuntimeException("NSD discovery failed to start: $errorCode"))
            }

            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {}
        }

        nsdManager.discoverServices(
            SettingsLAN.SERVICE_TYPE,
            NsdManager.PROTOCOL_DNS_SD,
            discoveryListener,
        )

        awaitClose {
            runCatching { nsdManager.stopServiceDiscovery(discoveryListener) }
        }
    }

    companion object {
        fun generateRoomId(): String = UUID.randomUUID().toString().take(8)
    }
}
