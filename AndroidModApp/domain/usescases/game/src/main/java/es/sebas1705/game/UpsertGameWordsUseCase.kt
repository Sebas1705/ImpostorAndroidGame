package es.sebas1705.game

import es.sebas1705.couchbase.documents.GameWordEntryDoc
import es.sebas1705.models.AppLanguage
import es.sebas1705.models.GameWordEntry
import es.sebas1705.repositories.interfaces.IGameWordRepository
import javax.inject.Inject

/**
 * Seeds or replaces localized game words in the target Couchbase language collection.
 */
class UpsertGameWordsUseCase @Inject constructor(
    private val gameWordRepository: IGameWordRepository
) {
    suspend operator fun invoke(
        language: AppLanguage,
        entries: List<GameWordEntry>
    ): Boolean {
        val docs = entries.map { entry ->
            GameWordEntryDoc(
                id = buildId(entry),
                word = entry.word,
                clue = entry.clue,
                category = entry.category.name
            )
        }
        return gameWordRepository.upsertAll(language.code, docs)
    }

    private fun buildId(entry: GameWordEntry): String =
        "${entry.category.name}_${entry.word.trim().lowercase().replace(" ", "_")}"
}

