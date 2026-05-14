package es.sebas1705.offlinegame.models

data class OfflineGameResult(
    val winner: OfflineWinner,
    val reason: String,
    val word: String,
    val impostorNames: List<String>,
    val correctVotes: Int,
    val incorrectVotes: Int
)