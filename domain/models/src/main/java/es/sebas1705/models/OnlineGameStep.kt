package es.sebas1705.models

import kotlinx.serialization.Serializable

@Serializable
enum class OnlineGameStep {
    Lobby,
    Reveal,
    Discussion,
    Result,
}
