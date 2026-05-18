package es.sebas1705.datastore.datasources

import androidx.datastore.core.DataStore
import es.sebas1705.datastore.GamePreferences
import es.sebas1705.datastore.model.GameData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GamePreferencesDataSource @Inject constructor(
    private val gamePreferences: DataStore<GamePreferences>
) {

    init {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            runCatching {
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
                                if (it.discussionTimerSeconds <= 0) {
                                    discussionTimerSeconds = DEFAULT_DISCUSSION_TIMER_SECONDS
                                }
                                setDefaultSet(true)
                            }
                            .build()
                    }
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
            showImpostorsInResult = it.showImpostorsInResult,
            discussionTimerSeconds = it.discussionTimerSeconds,
            impostorsKnowEachOther = it.impostorsKnowEachOther,
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
            .setDiscussionTimerSeconds(gameData.discussionTimerSeconds.coerceAtLeast(0))
            .setImpostorsKnowEachOther(gameData.impostorsKnowEachOther)
            .build()
    }

    suspend fun saveMode(
        mode: String,
        impostors: Int,
        showImpostorsInResult: Boolean,
        discussionTimerSeconds: Int,
        impostorsKnowEachOther: Boolean,
        showNumOfImpostors: Boolean
    ) = gamePreferences.updateData {
        it.toBuilder()
            .setMode(mode)
            .setImpostors(impostors.coerceAtLeast(DEFAULT_IMPOSTORS))
            .setShowImpostorsInResult(showImpostorsInResult)
            .setDiscussionTimerSeconds(discussionTimerSeconds.coerceAtLeast(0))
            .setImpostorsKnowEachOther(impostorsKnowEachOther)
            .setShowNumOfImpostors(showNumOfImpostors)
            .build()
    }

    fun getGameData() = gameData

    suspend fun saveNickname(nickname: String) = gamePreferences.updateData {
        it.toBuilder().setNickname(nickname).build()
    }

    fun getNickname(): kotlinx.coroutines.flow.Flow<String> = gamePreferences.data.map { it.nickname }

    private companion object {
        val DEFAULT_PLAYERS = listOf("Player1", "Player2", "Player3", "Player4")
        // Must match Modes.Classic.name — keep in sync if the enum is renamed
        const val DEFAULT_MODE = "Classic"
        const val DEFAULT_IMPOSTORS = 1
        const val DEFAULT_SHOW_IMPOSTORS_IN_RESULT = true
        const val DEFAULT_DISCUSSION_TIMER_SECONDS = 180
    }
}
