package es.sebas1705.repositories.interfaces

import es.sebas1705.retrofit.opendb.config.SettingsOpendb.TRIVIA_ENCODE_TEXT
import es.sebas1705.retrofit.opendb.dtos.ResponseOpendbDto

/**
 * Repository interface to get trivia questions
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
interface IOpendbRepository {

    /**
     * Get a list of trivia questions
     *
     * @param amount [Int]: Number of questions to get
     * @param category [Int]?: Category of the questions
     * @param difficulty [String]?: Difficulty of the questions
     * @param type [String]?: Type of the questions
     * @param encode [String]?: Encode of the questions
     *
     * @return [ResponseOpendbDto] with the list of questions
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    suspend fun getQuestions(
        amount: Int,
        category: Int? = null,
        difficulty: String? = null,
        type: String? = null,
        encode: String? = TRIVIA_ENCODE_TEXT
    ): ResponseOpendbDto

    /**
     * Get a list of 10 random trivia questions
     *
     * @return [ResponseOpendbDto] with the list of questions
     *
     * @see ResponseOpendbDto
     */
    suspend fun getTenRandomQuestions(): ResponseOpendbDto
}