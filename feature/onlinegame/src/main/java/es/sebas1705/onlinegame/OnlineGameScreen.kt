package es.sebas1705.onlinegame

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.sebas1705.onlinegame.screens.OnlineGamePlayScreen
import es.sebas1705.onlinegame.screens.OnlineLobbyScreen
import es.sebas1705.onlinegame.screens.OnlineModeSelectionScreen
import es.sebas1705.onlinegame.screens.OnlineWaitingRoomScreen
import es.sebas1705.onlinegame.viewmodel.OnlineGameIntent
import es.sebas1705.onlinegame.viewmodel.OnlineGameViewModel
import es.sebas1705.onlinegame.viewmodel.OnlineScreen

@Composable
fun OnlineGameScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnlineGameViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (state.screen) {
        OnlineScreen.ModeSelection -> OnlineModeSelectionScreen(
            onSelectMode = { viewModel.eventHandler(OnlineGameIntent.SelectMode(it)) },
            onBack = onBack,
            modifier = modifier.fillMaxSize(),
        )

        OnlineScreen.Lobby -> OnlineLobbyScreen(
            rooms = state.availableRooms,
            isLoading = state.isLoading,
            selectedMode = state.selectedMode ?: return,
            errorMessage = state.errorMessage,
            onCreateRoom = { name, max ->
                viewModel.eventHandler(OnlineGameIntent.CreateRoom(name, max))
            },
            onJoinRoom = { room, name ->
                viewModel.eventHandler(OnlineGameIntent.JoinRoom(room, name))
            },
            onBack = { viewModel.eventHandler(OnlineGameIntent.Disconnect) },
            modifier = modifier.fillMaxSize(),
        )

        OnlineScreen.WaitingRoom -> OnlineWaitingRoomScreen(
            connectedPlayers = state.connectedPlayers,
            isHost = state.isHost,
            onStartGame = { viewModel.eventHandler(OnlineGameIntent.StartGame) },
            onLeave = { viewModel.eventHandler(OnlineGameIntent.Disconnect); onBack() },
            modifier = modifier.fillMaxSize(),
        )

        OnlineScreen.Game -> OnlineGamePlayScreen(
            state = state.gameState,
            isHost = state.isHost,
            onMarkRevealDone = { viewModel.eventHandler(OnlineGameIntent.MarkRevealDone) },
            onNextRevealPlayer = { viewModel.eventHandler(OnlineGameIntent.NextRevealPlayer) },
            onVotePlayer = { viewModel.eventHandler(OnlineGameIntent.VotePlayer(it)) },
            onSubmitGuess = { viewModel.eventHandler(OnlineGameIntent.SubmitGuess(it)) },
            onLeave = { viewModel.eventHandler(OnlineGameIntent.Disconnect); onBack() },
            modifier = modifier.fillMaxSize(),
        )
    }
}
