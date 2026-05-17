package es.sebas1705.models

import kotlinx.serialization.Serializable

@Serializable
data class OnlinePlayer(
    val id: String,
    val name: String,
    val isHost: Boolean = false,
    val isReady: Boolean = false,
)
