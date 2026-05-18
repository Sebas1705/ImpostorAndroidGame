package es.sebas1705.game.nickname

import es.sebas1705.datastore.datasources.GamePreferencesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNicknameUseCase @Inject constructor(
    private val gamePreferencesDataSource: GamePreferencesDataSource,
) {
    operator fun invoke(): Flow<String> = gamePreferencesDataSource.getNickname()
}
