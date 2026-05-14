package es.sebas1705.models

data class OfflineRankingModel(
    val playerName: String,
    val civilianWins: Int,
    val impostorWins: Int,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0
) {
    val totalWins: Int
        get() = civilianWins + impostorWins
}

