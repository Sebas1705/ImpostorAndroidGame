package es.sebas1705.game.settings

import es.sebas1705.mappers.toData
import es.sebas1705.models.GameModel
import es.sebas1705.repositories.interfaces.IGameSettingsRepository
import javax.inject.Inject

class UpdateGameUseCase @Inject constructor(
    private val gameRepository: IGameSettingsRepository
) {
    suspend operator fun invoke(
        gameModel: GameModel
    ) = gameRepository.update(
        gameModel.toData()
    )
}

