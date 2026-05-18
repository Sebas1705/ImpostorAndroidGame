package es.sebas1705.game.nickname

import es.sebas1705.datastore.datasources.GamePreferencesDataSource
import javax.inject.Inject

class SaveNicknameUseCase @Inject constructor(
    private val gamePreferencesDataSource: GamePreferencesDataSource,
) {
    suspend operator fun invoke(nickname: String) =
        gamePreferencesDataSource.saveNickname(nickname)
}
