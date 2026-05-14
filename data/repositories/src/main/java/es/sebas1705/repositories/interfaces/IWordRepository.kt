package es.sebas1705.repositories.interfaces

import es.sebas1705.couchbase.documents.WordDoc

interface IWordRepository {

    suspend fun findByCategories(
        languageCode: String,
        categories: Set<String>
    ): List<WordDoc>

    suspend fun upsertAll(
        languageCode: String,
        entries: List<WordDoc>
    ): Boolean

    suspend fun importDefaultFromFiles(): Boolean

    suspend fun resetToDefault(): Boolean
}

