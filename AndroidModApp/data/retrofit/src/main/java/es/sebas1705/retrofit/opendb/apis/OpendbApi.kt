package es.sebas1705.retrofit.opendb.apis

import es.sebas1705.retrofit.opendb.dtos.ResponseOpendbDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface to manage the Opentbd API
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
interface OpendbApi {

    /**
     * Get trivia questions
     *
     * @param amount [Int]: Amount of questions to get
     * @param category [Int]: Category of the questions
     * @param difficulty [String]: Difficulty of the questions
     * @param type [String]: Type of the questions
     * @param encode [String]: Text to encode the URL
     *
     * @return [ResponseOpendbDto]: Response from the API
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @GET("api.php")
    suspend fun getTrivia(
        @Query("amount") amount: Int,
        @Query("category") category: Int? = null,
        @Query("difficulty") difficulty: String? = null,
        @Query("type") type: String? = null,
        @Query("encode") encode: String? = null
    ): ResponseOpendbDto

}

