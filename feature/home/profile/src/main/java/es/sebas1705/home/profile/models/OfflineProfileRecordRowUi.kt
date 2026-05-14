package es.sebas1705.home.profile.models

data class OfflineProfileRecordRowUi(
    val position: Int,
    val playerName: String,
    val civilianWins: Int,
    val impostorWins: Int,
    val totalWins: Int,
    val currentStreak: Int
)

