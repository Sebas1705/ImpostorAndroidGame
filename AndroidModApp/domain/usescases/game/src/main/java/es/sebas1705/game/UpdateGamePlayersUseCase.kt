package es.sebas1705.game

import es.sebas1705.repositories.interfaces.IGameRepository
import javax.inject.Inject

class UpdateGamePlayersUseCase @Inject constructor(
    private val gameRepository: IGameRepository
) {
    suspend operator fun invoke(
        players: List<String>
    ) = gameRepository.updatePlayers(players)
}

