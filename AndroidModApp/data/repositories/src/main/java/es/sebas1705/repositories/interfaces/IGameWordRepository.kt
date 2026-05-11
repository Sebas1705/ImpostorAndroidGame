package es.sebas1705.repositories.interfaces

import es.sebas1705.couchbase.documents.GameWordEntryDoc

interface IGameWordRepository {

    suspend fun findByCategories(
        languageCode: String,
        categories: Set<String>
    ): List<GameWordEntryDoc>

    suspend fun upsertAll(
        languageCode: String,
        entries: List<GameWordEntryDoc>
    ): Boolean

    suspend fun importDefaultFromFiles(): Boolean
}

