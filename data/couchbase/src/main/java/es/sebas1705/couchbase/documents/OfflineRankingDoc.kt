package es.sebas1705.couchbase.documents

import es.sebas1705.couchbase.documents.abstracts.Document

data class OfflineRankingDoc(
    val id: String,
    val playerName: String,
    val civilianWins: Int,
    val impostorWins: Int,
    val currentStreak: Int? = 0,
    val bestStreak: Int? = 0
) : Document(id)

