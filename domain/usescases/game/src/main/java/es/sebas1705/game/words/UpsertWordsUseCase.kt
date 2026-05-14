package es.sebas1705.game.words

import es.sebas1705.couchbase.documents.WordDoc
import es.sebas1705.models.AppLanguage
import es.sebas1705.models.WordModel
import es.sebas1705.repositories.interfaces.IWordRepository
import javax.inject.Inject

/**
 * Seeds or replaces localized game words in the target Couchbase language collection.
 */
class UpsertWordsUseCase @Inject constructor(
    private val gameWordRepository: IWordRepository
) {
    suspend operator fun invoke(
        language: AppLanguage,
        entries: List<WordModel>
    ): Boolean {
        val docs = entries.map { entry ->
            WordDoc(
                id = buildId(entry),
                word = entry.word,
                clue = entry.clue,
                category = entry.category.name
            )
        }
        return gameWordRepository.upsertAll(language.code, docs)
    }

    private fun buildId(entry: WordModel): String =
        "${entry.category.name}_${entry.word.trim().lowercase().replace(" ", "_")}"
}

