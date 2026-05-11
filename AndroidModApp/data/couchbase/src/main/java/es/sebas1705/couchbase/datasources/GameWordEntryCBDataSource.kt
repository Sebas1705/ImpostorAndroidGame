package es.sebas1705.couchbase.datasources

import es.sebas1705.couchbase.documents.GameWordEntryDoc
import es.sebas1705.couchbase.documents.abstracts.fromMap
import es.sebas1705.couchbase.manager.ICouchbaseManager
import javax.inject.Inject

class GameWordEntryCBDataSource @Inject constructor(
    private val couchbaseManager: ICouchbaseManager
) {

    fun getAll(languageCode: String): List<GameWordEntryDoc> =
        couchbaseManager.getAll(collectionName(languageCode)).map { it.fromMap(GameWordEntryDoc::class) }

    fun getByCategory(languageCode: String, category: String): List<GameWordEntryDoc> =
        couchbaseManager.getByParam(collectionName(languageCode), PARAM_CATEGORY, category)
            .map { it.fromMap(GameWordEntryDoc::class) }

    fun upsertAll(languageCode: String, entries: List<GameWordEntryDoc>): Boolean =
        couchbaseManager.upsertAll(
            collectionName = collectionName(languageCode),
            documents = entries.map { it.asMap() },
            ids = entries.map { it.id }
        )

    private fun collectionName(languageCode: String): String =
        when (languageCode.lowercase()) {
            LANGUAGE_EN -> COLLECTION_EN
            else -> COLLECTION_ES
        }

    private companion object {
        const val PARAM_CATEGORY = "category"
        const val LANGUAGE_EN = "en"
        const val COLLECTION_ES = "game_word_entry_es_collection"
        const val COLLECTION_EN = "game_word_entry_en_collection"
    }
}

