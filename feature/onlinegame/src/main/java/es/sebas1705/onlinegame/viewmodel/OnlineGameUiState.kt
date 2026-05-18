package es.sebas1705.onlinegame.viewmodel

import es.sebas1705.common.mvi.MVIBaseState
import es.sebas1705.models.Categories
import es.sebas1705.models.GameRoom
import es.sebas1705.models.Modes
import es.sebas1705.models.NetworkMode
import es.sebas1705.models.OnlineGameState
import es.sebas1705.models.OnlinePlayer

data class OnlineGameUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val savedNickname: String = "",
    val screen: OnlineScreen = OnlineScreen.Lobby,
    // ── Config (editable from FaceScreen and WaitingRoom) ─────────────────
    val networkMode: NetworkMode = NetworkMode.Local,
    val categories: Set<Categories> = emptySet(),
    val mode: Modes = Modes.Classic,
    val impostors: Int = 1,
    val discussionTimerSeconds: Int = 180,
    val impostorsKnowEachOther: Boolean = false,
    val showNumOfImpostors: Boolean = false,
    // ── Lobby ──────────────────────────────────────────────────────────────
    val availableRooms: List<GameRoom> = emptyList(),
    // ── Room / game ────────────────────────────────────────────────────────
    val currentRoom: GameRoom? = null,
    val connectedPlayers: List<OnlinePlayer> = emptyList(),
    val isHost: Boolean = false,
    val localPlayer: OnlinePlayer? = null,
    val gameState: OnlineGameState = OnlineGameState(),
) : MVIBaseState

enum class OnlineScreen {
    Lobby,
    WaitingRoom,
    Reveal,
    Discussion,
    Result,
}
