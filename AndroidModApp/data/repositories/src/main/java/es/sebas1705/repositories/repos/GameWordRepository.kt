package es.sebas1705.repositories.repos

import es.sebas1705.couchbase.datasources.GameWordEntryCBDataSource
import es.sebas1705.couchbase.documents.GameWordEntryDoc
import es.sebas1705.repositories.interfaces.IFileRepository
import es.sebas1705.repositories.interfaces.IGameWordRepository
import java.util.Locale
import javax.inject.Inject

class GameWordRepository @Inject constructor(
    private val gameWordEntryCBDataSource: GameWordEntryCBDataSource,
    private val fileRepository: IFileRepository
) : IGameWordRepository {

    override suspend fun findByCategories(
        languageCode: String,
        categories: Set<String>
    ): List<GameWordEntryDoc> {
        if (categories.isEmpty()) {
            return gameWordEntryCBDataSource.getAll(languageCode)
        }

        return categories
            .flatMap { category -> gameWordEntryCBDataSource.getByCategory(languageCode, category) }
            .distinctBy { it.id }
    }

    override suspend fun upsertAll(
        languageCode: String,
        entries: List<GameWordEntryDoc>
    ): Boolean = gameWordEntryCBDataSource.upsertAll(languageCode, entries)

    override suspend fun importDefaultFromFiles(): Boolean {
        val groupedByLanguage = fileRepository
            .readDefaultGameWords()
            .groupBy { it.languageCode }

        return groupedByLanguage.entries.all { (languageCode, entries) ->
            val docs = entries.map { entry ->
                val normalizedCategory = entry.category
                    .trim()
                    .replace('-', '_')
                    .uppercase(Locale.ROOT)

                GameWordEntryDoc(
                    id = "${normalizedCategory}_${entry.word.trim().lowercase().replace(" ", "_")}",
                    word = entry.word,
                    clue = entry.clues.filter { it.isNotBlank() }.take(MAX_CLUES),
                    category = normalizedCategory
                )
            }

            gameWordEntryCBDataSource.upsertAll(languageCode, docs)
        }
    }

    private companion object {
        const val MAX_CLUES = 5
    }
}

