package es.sebas1705.game.settings

import es.sebas1705.models.Modes
import es.sebas1705.repositories.interfaces.IGameSettingsRepository
import javax.inject.Inject

class UpdateGameModeUseCase @Inject constructor(
    private val gameRepository: IGameSettingsRepository
) {
    suspend operator fun invoke(
        mode: Modes,
        impostors: Int,
        showImpostorsInResult: Boolean,
        discussionTimerSeconds: Int,
        impostorsKnowEachOther: Boolean,
        showNumOfImpostors: Boolean
    ) = gameRepository.updateMode(
        mode.name,
        impostors,
        showImpostorsInResult,
        discussionTimerSeconds,
        impostorsKnowEachOther,
        showNumOfImpostors
    )
}

