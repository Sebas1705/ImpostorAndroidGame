package es.sebas1705.models

import kotlinx.serialization.Serializable

@Serializable
data class OnlineGameResult(
    val winner: OnlineWinner,
    val reason: String,
    val word: String,
    val impostorNames: List<String>,
    val correctVotes: Int,
    val incorrectVotes: Int,
)
