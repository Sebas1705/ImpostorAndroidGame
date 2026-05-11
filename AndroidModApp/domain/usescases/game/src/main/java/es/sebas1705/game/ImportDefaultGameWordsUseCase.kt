package es.sebas1705.game

import es.sebas1705.repositories.interfaces.IGameWordRepository
import javax.inject.Inject

/**
 * Imports the default game word JSON database from assets into Couchbase.
 */
class ImportDefaultGameWordsUseCase @Inject constructor(
    private val gameWordRepository: IGameWordRepository
) {
    suspend operator fun invoke(): Boolean =
        gameWordRepository.importDefaultFromFiles()
}

