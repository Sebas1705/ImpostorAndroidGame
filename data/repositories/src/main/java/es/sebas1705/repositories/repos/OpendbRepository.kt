package es.sebas1705.repositories.repos

import es.sebas1705.repositories.interfaces.IOpendbRepository
import es.sebas1705.retrofit.opendb.datasources.OpendbApiDataSource
import es.sebas1705.retrofit.opendb.dtos.ResponseOpendbDto
import javax.inject.Inject

/**
 * Trivia repository implementation
 *
 * @property opendbApiDataSource [OpendbApiDataSource]: Data source to get the questions from the API
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class OpendbRepository @Inject constructor(
    private val opendbApiDataSource: OpendbApiDataSource
) : IOpendbRepository {

    override suspend fun getQuestions(
        amount: Int,
        category: Int?,
        difficulty: String?,
        type: String?,
        encode: String?
    ): ResponseOpendbDto {
        return opendbApiDataSource.getQuestions(
            amount,
            category,
            difficulty,
            type,
            encode
        )
    }


    override suspend fun getTenRandomQuestions(): ResponseOpendbDto {
        return opendbApiDataSource.getTenRandomQuestions()
    }


}