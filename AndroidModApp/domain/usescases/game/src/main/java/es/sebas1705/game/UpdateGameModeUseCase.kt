package es.sebas1705.game

import es.sebas1705.models.Modes
import es.sebas1705.repositories.interfaces.IGameRepository
import javax.inject.Inject

class UpdateGameModeUseCase @Inject constructor(
    private val gameRepository: IGameRepository
) {
    suspend operator fun invoke(
        mode: Modes,
        impostors: Int,
        showImpostorsInResult: Boolean
    ) = gameRepository.updateMode(
        mode.name,
        impostors,
        showImpostorsInResult
    )
}

