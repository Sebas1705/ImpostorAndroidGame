package es.sebas1705.couchbase.documents

import es.sebas1705.couchbase.documents.abstracts.Document

data class OfflineRankingEntryDoc(
    val id: String,
    val playerName: String,
    val civilianWins: Int,
    val impostorWins: Int
) : Document(id)

