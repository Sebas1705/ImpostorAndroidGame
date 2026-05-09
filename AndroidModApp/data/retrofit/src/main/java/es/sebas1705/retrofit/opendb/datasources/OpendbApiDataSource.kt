package es.sebas1705.retrofit.opendb.datasources

import es.sebas1705.retrofit.opendb.apis.OpendbApi
import es.sebas1705.retrofit.opendb.config.SettingsOpendb.TRIVIA_ENCODE_TEXT
import es.sebas1705.retrofit.opendb.dtos.ResponseOpendbDto
import javax.inject.Inject

class OpendbApiDataSource @Inject constructor(
    private val opendbApi: OpendbApi
) {

    /**
     * Fetches trivia questions from the OpenDB API.
     *
     * @param amount Number of questions to fetch.
     * @param category Optional category ID for filtering questions.
     * @param difficulty Optional difficulty level (easy, medium, hard).
     * @param type Optional type of questions (multiple choice, true/false).
     * @param encode Optional encoding format for the questions.
     *
     * @return [ResponseOpendbDto] containing the trivia questions.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    suspend fun getQuestions(
        amount: Int,
        category: Int?,
        difficulty: String?,
        type: String?,
        encode: String?
    ): ResponseOpendbDto {
        return opendbApi.getTrivia(
            amount,
            category,
            difficulty,
            type,
            encode
        )
    }

    /**
     * Fetches ten random trivia questions from the OpenDB API.
     *
     * @return [ResponseOpendbDto] containing ten random trivia questions.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    suspend fun getTenRandomQuestions(): ResponseOpendbDto {
        return opendbApi.getTrivia(10, encode = TRIVIA_ENCODE_TEXT)
    }


}