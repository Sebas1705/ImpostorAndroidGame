package es.sebas1705.home.ranking.models

data class OfflineRankingRowUi(
    val position: Int,
    val playerName: String,
    val civilianWins: Int,
    val impostorWins: Int,
    val totalWins: Int
)

