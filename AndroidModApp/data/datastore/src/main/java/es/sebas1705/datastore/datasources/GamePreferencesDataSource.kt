package es.sebas1705.datastore.datasources

import androidx.datastore.core.DataStore
import es.sebas1705.datastore.GamePreferences
import es.sebas1705.datastore.model.GameData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GamePreferencesDataSource @Inject constructor(
    private val gamePreferences: DataStore<GamePreferences>
) {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            gamePreferences.updateData {
                if (it.defaultSet) {
                    it
                } else {
                    it.toBuilder()
                        .apply {
                            if (it.playersCount == 0) {
                                addAllPlayers(DEFAULT_PLAYERS)
                            }
                            if (it.mode.isEmpty()) {
                                mode = DEFAULT_MODE
                            }
                            if (it.impostors <= 0) {
                                impostors = DEFAULT_IMPOSTORS
                            }
                            showImpostorsInResult = DEFAULT_SHOW_IMPOSTORS_IN_RESULT
                            setDefaultSet(true)
                        }
                        .build()
                }
            }
        }
    }

    private val gameData = gamePreferences.data.map {
        GameData(
            selectedCategories = it.selectedCategoriesList,
            players = it.playersList,
            mode = it.mode,
            impostors = it.impostors,
            showImpostorsInResult = it.showImpostorsInResult
        )
    }

    suspend fun saveSelectedCategories(
        selectedCategories: List<String>
    ) = gamePreferences.updateData {
        it.toBuilder()
            .clearSelectedCategories()
            .addAllSelectedCategories(selectedCategories)
            .build()
    }

    suspend fun savePlayers(
        players: List<String>
    ) = gamePreferences.updateData {
        it.toBuilder()
            .clearPlayers()
            .addAllPlayers(players)
            .build()
    }

    suspend fun saveGameData(
        gameData: GameData
    ) = gamePreferences.updateData {
        it.toBuilder()
            .clearSelectedCategories()
            .addAllSelectedCategories(gameData.selectedCategories)
            .clearPlayers()
            .addAllPlayers(gameData.players)
            .setMode(gameData.mode)
            .setImpostors(gameData.impostors.coerceAtLeast(DEFAULT_IMPOSTORS))
            .setShowImpostorsInResult(gameData.showImpostorsInResult)
            .build()
    }

    suspend fun saveMode(
        mode: String,
        impostors: Int,
        showImpostorsInResult: Boolean
    ) = gamePreferences.updateData {
        it.toBuilder()
            .setMode(mode)
            .setImpostors(impostors.coerceAtLeast(DEFAULT_IMPOSTORS))
            .setShowImpostorsInResult(showImpostorsInResult)
            .build()
    }

    fun getGameData() = gameData

    private companion object {
        val DEFAULT_PLAYERS = listOf("Player1", "Player2")
        const val DEFAULT_MODE = "Classic"
        const val DEFAULT_IMPOSTORS = 1
        const val DEFAULT_SHOW_IMPOSTORS_IN_RESULT = true
    }
}
