package es.sebas1705.game.settings

import es.sebas1705.repositories.interfaces.IGameSettingsRepository
import javax.inject.Inject

class UpdateGamePlayersUseCase @Inject constructor(
    private val gameRepository: IGameSettingsRepository
) {
    suspend operator fun invoke(
        players: List<String>
    ) = gameRepository.updatePlayers(players)
}

