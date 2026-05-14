package es.sebas1705.game.words

import es.sebas1705.couchbase.documents.WordDoc
import es.sebas1705.models.AppLanguage
import es.sebas1705.models.Categories
import es.sebas1705.models.WordsDbStatsModel
import es.sebas1705.repositories.interfaces.IAppSettingsRepository
import es.sebas1705.repositories.interfaces.IWordRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetWordsDbStatsUseCase @Inject constructor(
    private val gameWordRepository: IWordRepository,
    private val settingsRepository: IAppSettingsRepository,
) {
    suspend operator fun invoke(): WordsDbStatsModel {

        val wordsEs = gameWordRepository.findByCategories(AppLanguage.Spanish.code, emptySet())
        val wordsEn = gameWordRepository.findByCategories(AppLanguage.English.code, emptySet())
        val selectedLanguage = settingsRepository.read().first().appLanguage
        val selectedWords = if (selectedLanguage == AppLanguage.English.code)
            wordsEn
        else wordsEs

        val topCategory = topCategory(selectedWords)
        val averageClues = averageClues(selectedWords)
        val duplicateWordsCount = duplicateWordsCount(selectedWords)
        val pureDuplicateEntriesCount = pureDuplicateEntriesCount(selectedWords)
        val invalidClueEntriesCount = invalidClueEntriesCount(selectedWords)
        val validClueCoverage = validClueCoverage(selectedWords, invalidClueEntriesCount)

        val categoriesEsCount = wordsEs.map { it.category }.toSet().size
        val categoriesEnCount = wordsEn.map { it.category }.toSet().size
        val categoryCoverageEs = categoriesEsCount.toDouble() / Categories.entries.size.toDouble() * 100.0
        val categoryCoverageEn = categoriesEnCount.toDouble() / Categories.entries.size.toDouble() * 100.0

        val selectedCoverage = if (selectedLanguage == AppLanguage.English.code)
            categoryCoverageEn
        else categoryCoverageEs

        val missingCategoriesInSelectedLanguage =
            (Categories.entries.size - selectedWords.map { it.category }.toSet().size).coerceAtLeast(0)

        return WordsDbStatsModel(
            selectedLanguage = selectedLanguage,
            totalWordsEs = wordsEs.size,
            totalWordsEn = wordsEn.size,
            selectedLanguageWords = selectedWords.size,
            categoriesEs = categoriesEsCount,
            categoriesEn = categoriesEnCount,
            categoryCoverageEs = categoryCoverageEs,
            categoryCoverageEn = categoryCoverageEn,
            categoryCoverageSelectedLanguage = selectedCoverage,
            averageCluesInSelectedLanguage = averageClues,
            duplicateWordsInSelectedLanguage = duplicateWordsCount,
            pureDuplicateEntriesInSelectedLanguage = pureDuplicateEntriesCount,
            invalidClueEntriesInSelectedLanguage = invalidClueEntriesCount,
            missingCategoriesInSelectedLanguage = missingCategoriesInSelectedLanguage,
            validClueEntriesCoverageInSelectedLanguage = validClueCoverage,
            latestWordInSelectedLanguage = selectedWords.firstOrNull()?.word,
            topCategoryInSelectedLanguage = topCategory
        )
    }

    private companion object {
        const val EXPECTED_CLUES_PER_WORD = 5
    }

    private fun topCategory(selectedWords: List<WordDoc>): String? =
        selectedWords
            .groupingBy { it.category }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key

    private fun averageClues(selectedWords: List<WordDoc>): Double =
        if (selectedWords.isEmpty()) 0.0 else selectedWords.map { it.clue.size }.average()

    private fun duplicateWordsCount(selectedWords: List<WordDoc>): Int =
        selectedWords
            .groupingBy { it.word.trim().lowercase() }
            .eachCount()
            .values
            .sumOf { count -> (count - 1).coerceAtLeast(0) }

    private fun pureDuplicateEntriesCount(selectedWords: List<WordDoc>): Int =
        selectedWords
            .groupingBy { word ->
                PureDuplicateKey(
                    word = word.word,
                    category = word.category,
                    clues = word.clue
                )
            }
            .eachCount()
            .values
            .sumOf { count -> (count - 1).coerceAtLeast(0) }

    private fun invalidClueEntriesCount(selectedWords: List<WordDoc>): Int =
        selectedWords.count { entry ->
            entry.clue.size != EXPECTED_CLUES_PER_WORD || entry.clue.any { clue -> clue.isBlank() }
        }

    private fun validClueCoverage(
        selectedWords: List<WordDoc>,
        invalidClueEntriesCount: Int
    ): Double = if (selectedWords.isEmpty()) {
        0.0
    } else {
        val validEntries = selectedWords.size - invalidClueEntriesCount
        validEntries.toDouble() / selectedWords.size.toDouble() * 100.0
    }

    private data class PureDuplicateKey(
        val word: String,
        val category: String,
        val clues: List<String>
    )
}





