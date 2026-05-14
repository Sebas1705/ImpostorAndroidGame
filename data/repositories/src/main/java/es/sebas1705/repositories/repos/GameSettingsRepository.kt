package es.sebas1705.repositories.repos

import es.sebas1705.datastore.datasources.GamePreferencesDataSource
import es.sebas1705.datastore.model.GameData
import es.sebas1705.repositories.interfaces.IGameSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GameSettingsRepository @Inject constructor(
    private val gamePreferencesDataSource: GamePreferencesDataSource
) : IGameSettingsRepository {

    override fun read(): Flow<GameData> =
        gamePreferencesDataSource.getGameData()

    override suspend fun update(gameData: GameData) {
        gamePreferencesDataSource.saveGameData(gameData)
    }

    override suspend fun updateSelectedCategories(selectedCategories: Set<String>) {
        gamePreferencesDataSource.saveSelectedCategories(selectedCategories.toList())
    }

    override suspend fun updatePlayers(players: List<String>) {
        gamePreferencesDataSource.savePlayers(players)
    }

    override suspend fun updateMode(mode: String, impostors: Int, showImpostorsInResult: Boolean) {
        gamePreferencesDataSource.saveMode(mode, impostors, showImpostorsInResult)
    }
}

