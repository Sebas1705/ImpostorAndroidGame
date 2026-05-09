package es.sebas1705.opendbusescases

import es.sebas1705.repositories.interfaces.IOpendbRepository
import es.sebas1705.retrofit.opendb.dtos.ResponseOpendbDto
import javax.inject.Inject

/**
 * Use case fot getting 10 random questions
 *
 * @param opendbRepository [opendbRepository] repository to get the questions
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class GetTriviaTenQuestionsUseCase @Inject constructor(
    private val opendbRepository: IOpendbRepository
) {

    suspend operator fun invoke(): ResponseOpendbDto {
        return opendbRepository.getTenRandomQuestions()
    }

}