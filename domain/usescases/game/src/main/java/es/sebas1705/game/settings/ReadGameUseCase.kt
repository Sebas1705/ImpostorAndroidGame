package es.sebas1705.game.settings

import es.sebas1705.mappers.toModel
import es.sebas1705.models.GameModel
import es.sebas1705.repositories.interfaces.IGameSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReadGameUseCase @Inject constructor(
    private val gameRepository: IGameSettingsRepository
) {
    operator fun invoke(): Flow<GameModel> =
        gameRepository.read().map { it.toModel() }
}

