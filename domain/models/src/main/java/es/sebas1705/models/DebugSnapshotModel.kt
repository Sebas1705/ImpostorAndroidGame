package es.sebas1705.models

data class DebugSnapshotModel(
    val createdAtEpochMs: Long,
    val selectedLanguage: String,
    val selectedLanguageWords: Int,
    val duplicateWords: Int,
    val pureDuplicates: Int,
    val invalidClueEntries: Int,
    val missingCategories: Int,
    val validClueCoverage: Double
)

