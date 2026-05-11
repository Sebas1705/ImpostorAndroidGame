package es.sebas1705.models

data class OfflineRankingEntry(
    val playerName: String,
    val civilianWins: Int,
    val impostorWins: Int
) {
    val totalWins: Int
        get() = civilianWins + impostorWins
}

