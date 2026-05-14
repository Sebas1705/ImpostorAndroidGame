package es.sebas1705.repositories.interfaces

import es.sebas1705.datastore.model.GameData
import kotlinx.coroutines.flow.Flow

interface IGameSettingsRepository {

    fun read(): Flow<GameData>

    suspend fun update(gameData: GameData)

    suspend fun updateSelectedCategories(selectedCategories: Set<String>)

    suspend fun updatePlayers(players: List<String>)

    suspend fun updateMode(mode: String, impostors: Int, showImpostorsInResult: Boolean)
}

