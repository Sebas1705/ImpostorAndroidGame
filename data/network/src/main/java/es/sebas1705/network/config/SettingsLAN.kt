package es.sebas1705.network.config

internal object SettingsLAN {
    const val SERVICE_TYPE = "_impostorgame._tcp."
    const val SERVICE_NAME_PREFIX = "ImpostorRoom"
    const val DEFAULT_PORT = 0 // 0 = let OS pick a free port
    const val CONNECT_TIMEOUT_MS = 5_000
    const val BUFFER_SIZE = 8192
    const val DISCOVERY_TIMEOUT_MS = 10_000L
}
