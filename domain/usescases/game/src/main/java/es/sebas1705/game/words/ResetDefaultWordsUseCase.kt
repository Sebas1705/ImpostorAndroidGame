package es.sebas1705.game.words

import es.sebas1705.repositories.interfaces.IWordRepository
import javax.inject.Inject

/**
 * Imports the default game word JSON database from assets into Couchbase.
 */
class ResetDefaultWordsUseCase @Inject constructor(
    private val gameWordRepository: IWordRepository
) {
    suspend operator fun invoke(): Boolean =
        gameWordRepository.resetToDefault()
}

