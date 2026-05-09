package es.sebas1705.retrofit.opendb.dtos

import com.google.gson.annotations.SerializedName

/**
 * DTO to represent a response from the Opentbd API
 *
 * @property responseCode [Int]: Response code of the API
 * @property questionOpendbDtos [List]<[QuestionOpendbDto]>: List of questions
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
data class ResponseOpendbDto(
    val responseCode: Int,
    @SerializedName("results") val questionOpendbDtos: List<QuestionOpendbDto>
)