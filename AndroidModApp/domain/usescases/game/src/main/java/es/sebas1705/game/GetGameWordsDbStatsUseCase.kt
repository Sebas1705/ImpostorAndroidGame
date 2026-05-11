package es.sebas1705.game

import es.sebas1705.models.Categories
import es.sebas1705.repositories.interfaces.IGameWordRepository
import es.sebas1705.repositories.interfaces.ISettingsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetGameWordsDbStatsUseCase @Inject constructor(
    private val gameWordRepository: IGameWordRepository,
    private val settingsRepository: ISettingsRepository,
    private val ensureGameWordsImportedUseCase: EnsureGameWordsImportedUseCase
) {
    suspend operator fun invoke(): GameWordsDbStats {
        ensureGameWordsImportedUseCase()

        val wordsEs = gameWordRepository.findByCategories(LANGUAGE_ES, emptySet())
        val wordsEn = gameWordRepository.findByCategories(LANGUAGE_EN, emptySet())
        val selectedLanguage = settingsRepository.read().first().appLanguage
        val selectedWords = if (selectedLanguage == LANGUAGE_EN) wordsEn else wordsEs

        val topCategory = selectedWords
            .groupingBy { it.category }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key

        val averageClues = if (selectedWords.isEmpty()) 0.0
        else selectedWords.map { it.clue.size }.average()

        val categoriesEsCount = wordsEs.map { it.category }.toSet().size
        val categoriesEnCount = wordsEn.map { it.category }.toSet().size
        val categoryCoverageEs = categoriesEsCount.toDouble() / Categories.entries.size.toDouble() * 100.0
        val categoryCoverageEn = categoriesEnCount.toDouble() / Categories.entries.size.toDouble() * 100.0
        val selectedCoverage = if (selectedLanguage == LANGUAGE_EN) categoryCoverageEn else categoryCoverageEs

        return GameWordsDbStats(
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
            latestWordInSelectedLanguage = selectedWords.firstOrNull()?.word,
            topCategoryInSelectedLanguage = topCategory
        )
    }

    private companion object {
        const val LANGUAGE_ES = "es"
        const val LANGUAGE_EN = "en"
    }
}

data class GameWordsDbStats(
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
    val latestWordInSelectedLanguage: String?,
    val topCategoryInSelectedLanguage: String?
)



