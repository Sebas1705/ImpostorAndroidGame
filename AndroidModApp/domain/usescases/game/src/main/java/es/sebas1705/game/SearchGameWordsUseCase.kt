package es.sebas1705.game

import es.sebas1705.models.Categories
import es.sebas1705.models.GameWordEntry
import es.sebas1705.repositories.interfaces.IGameWordRepository
import es.sebas1705.repositories.interfaces.ISettingsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Reads game words from the Couchbase language collection selected in app settings.
 */
class SearchGameWordsUseCase @Inject constructor(
    private val gameWordRepository: IGameWordRepository,
    private val settingsRepository: ISettingsRepository,
    private val ensureGameWordsImportedUseCase: EnsureGameWordsImportedUseCase
) {
    suspend operator fun invoke(
        categories: Set<Categories>
    ): List<GameWordEntry> {
        ensureGameWordsImportedUseCase()

        val settings = settingsRepository.read().first()


        return gameWordRepository
            .findByCategories(
                languageCode = settings.appLanguage,
                categories = categories.map { it.name }.toSet()
            )
            .mapNotNull { document ->
                val category = runCatching { Categories.valueOf(document.category) }.getOrNull()
                    ?: return@mapNotNull null
                GameWordEntry(
                    word = document.word,
                    clue = document.clue,
                    category = category
                )
            }
    }
}

