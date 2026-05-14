package es.sebas1705.couchbase.documents

import es.sebas1705.couchbase.documents.abstracts.Document

/**
 * Couchbase document storing historical debug metrics snapshots.
 */
data class DebugSnapshotDoc(
    val id: String,
    val createdAtEpochMs: Long,
    val selectedLanguage: String,
    val selectedLanguageWords: Int,
    val duplicateWords: Int,
    val pureDuplicates: Int,
    val invalidClueEntries: Int,
    val missingCategories: Int,
    val validClueCoverage: Double
) : Document(id)

