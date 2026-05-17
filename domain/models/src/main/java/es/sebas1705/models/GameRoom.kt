package es.sebas1705.models

import kotlinx.serialization.Serializable

@Serializable
data class GameRoom(
    val id: String,
    val hostName: String,
    val playerCount: Int,
    val maxPlayers: Int,
    val networkMode: NetworkMode,
    val hostAddress: String = "",
    val port: Int = 0,
)
