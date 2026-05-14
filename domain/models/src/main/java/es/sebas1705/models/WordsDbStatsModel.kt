package es.sebas1705.models

data class WordsDbStatsModel(
    val selectedLanguage: String,
    val totalWordsEs: Int,
    val totalWordsEn: Int,
    val selectedLanguageWords: Int,
    val categoriesEs: Int,
    val categoriesEn: Int,
    val categoryCoverageEs: Double,
    val categoryCoverageEn: Double,
    val categoryCoverageSelectedLanguage: Double,
    val averageCluesInSelectedLanguage: Double,
    val duplicateWordsInSelectedLanguage: Int,
    val pureDuplicateEntriesInSelectedLanguage: Int,
    val invalidClueEntriesInSelectedLanguage: Int,
    val missingCategoriesInSelectedLanguage: Int,
    val validClueEntriesCoverageInSelectedLanguage: Double,
    val latestWordInSelectedLanguage: String?,
    val topCategoryInSelectedLanguage: String?
)