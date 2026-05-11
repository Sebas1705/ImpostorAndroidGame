package es.sebas1705.game

import es.sebas1705.repositories.interfaces.IGameWordRepository
import javax.inject.Inject

/**
 * Ensures default game words are imported once when Couchbase collections are empty.
 */
class EnsureGameWordsImportedUseCase @Inject constructor(
    private val gameWordRepository: IGameWordRepository,
    private val importDefaultGameWordsUseCase: ImportDefaultGameWordsUseCase
) {
    suspend operator fun invoke() {
        val hasEs = gameWordRepository.findByCategories(LANGUAGE_ES, emptySet()).isNotEmpty()
        val hasEn = gameWordRepository.findByCategories(LANGUAGE_EN, emptySet()).isNotEmpty()

        if (!hasEs || !hasEn) {
            importDefaultGameWordsUseCase()
        }
    }

    private companion object {
        const val LANGUAGE_ES = "es"
        const val LANGUAGE_EN = "en"
    }
}

