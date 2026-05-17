package es.sebas1705.onlinegame.viewmodel

import es.sebas1705.common.mvi.MVIBaseState
import es.sebas1705.models.GameRoom
import es.sebas1705.models.NetworkMode
import es.sebas1705.models.OnlineGameState
import es.sebas1705.models.OnlinePlayer

data class OnlineGameUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val screen: OnlineScreen = OnlineScreen.ModeSelection,
    val selectedMode: NetworkMode? = null,
    val availableRooms: List<GameRoom> = emptyList(),
    val connectedPlayers: List<OnlinePlayer> = emptyList(),
    val currentRoom: GameRoom? = null,
    val isHost: Boolean = false,
    val gameState: OnlineGameState = OnlineGameState(),
) : MVIBaseState

enum class OnlineScreen {
    ModeSelection,
    Lobby,
    WaitingRoom,
    Game,
}
