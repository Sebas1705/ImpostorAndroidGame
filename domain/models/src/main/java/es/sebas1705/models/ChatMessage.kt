package es.sebas1705.models

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val playerId: String,
    val playerName: String,
    val content: String,
    val timestamp: Long,
)
