package es.sebas1705.models

import kotlinx.serialization.Serializable

@Serializable
enum class OnlineWinner {
    Civilians,
    Impostors,
    Tie,
}
