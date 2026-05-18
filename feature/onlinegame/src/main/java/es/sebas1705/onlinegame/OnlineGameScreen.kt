package es.sebas1705.onlinegame

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.sebas1705.models.Categories
import es.sebas1705.models.Modes
import es.sebas1705.models.NetworkMode
import es.sebas1705.onlinegame.screens.OnlineDiscussionScreen
import es.sebas1705.onlinegame.screens.OnlineLobbyScreen
import es.sebas1705.onlinegame.screens.OnlineResultScreen
import es.sebas1705.onlinegame.screens.OnlineRevealScreen
import es.sebas1705.onlinegame.screens.OnlineWaitingRoomScreen
import es.sebas1705.onlinegame.viewmodel.OnlineGameIntent
import es.sebas1705.onlinegame.viewmodel.OnlineGameViewModel
import es.sebas1705.onlinegame.viewmodel.OnlineScreen
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun OnlineGameScreen(
    onBack: () -> Unit,
    categories: ImmutableSet<Categories>,
    modeName: String,
    impostors: Int,
    networkMode: String,
    modifier: Modifier = Modifier,
    viewModel: OnlineGameViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Send Initialize once when the composable enters composition.
    LaunchedEffect(Unit) {
        val mode = Modes.entries.firstOrNull { it.name == modeName } ?: Modes.Classic
        val net = NetworkMode.entries.firstOrNull { it.name == networkMode } ?: NetworkMode.Local
        viewModel.eventHandler(
            OnlineGameIntent.Initialize(
                categories = categories.toSet(),
                mode = mode,
                impostors = impostors,
                networkMode = net,
            )
        )
    }

    when (state.screen) {
        OnlineScreen.Lobby -> OnlineLobbyScreen(
            rooms = state.availableRooms,
            isLoading = state.isLoading,
            selectedMode = state.networkMode,
            errorMessage = state.errorMessage,
            onCreateRoom = { name, max ->
                viewModel.eventHandler(OnlineGameIntent.CreateRoom(name, max))
            },
            onJoinRoom = { room, name ->
                viewModel.eventHandler(OnlineGameIntent.JoinRoom(room, name))
            },
            onBack = {
                viewModel.eventHandler(OnlineGameIntent.Disconnect)
                onBack()
            },
            modifier = modifier.fillMaxSize(),
            savedNickname = state.savedNickname,
        )

        OnlineScreen.WaitingRoom -> OnlineWaitingRoomScreen(
            connectedPlayers = state.connectedPlayers,
            isHost = state.isHost,
            categories = state.categories,
            mode = state.mode,
            impostors = state.impostors,
            discussionTimerSeconds = state.discussionTimerSeconds,
            impostorsKnowEachOther = state.impostorsKnowEachOther,
            showNumOfImpostors = state.showNumOfImpostors,
            onUpdateConfig = { cats, m, imp, timer, know, showNum ->
                viewModel.eventHandler(
                    OnlineGameIntent.UpdateGameConfig(
                        categories = cats,
                        mode = m,
                        impostors = imp,
                        discussionTimerSeconds = timer,
                        impostorsKnowEachOther = know,
                        showNumOfImpostors = showNum,
                    )
                )
            },
            onStartGame = { viewModel.eventHandler(OnlineGameIntent.StartGame) },
            onLeave = {
                viewModel.eventHandler(OnlineGameIntent.Disconnect)
                onBack()
            },
            modifier = modifier.fillMaxSize(),
        )

        OnlineScreen.Reveal -> OnlineRevealScreen(
            state = state.gameState,
            onMarkRevealDone = { viewModel.eventHandler(OnlineGameIntent.MarkRevealDone) },
            onLeave = {
                viewModel.eventHandler(OnlineGameIntent.Disconnect)
                onBack()
            },
            modifier = modifier.fillMaxSize(),
        )

        OnlineScreen.Discussion -> OnlineDiscussionScreen(
            state = state.gameState,
            localPlayer = state.localPlayer,
            isHost = state.isHost,
            onSendChatMessage = { viewModel.eventHandler(OnlineGameIntent.SendChatMessage(it)) },
            onVotePlayer = { viewModel.eventHandler(OnlineGameIntent.VotePlayer(it)) },
            onSubmitGuess = { viewModel.eventHandler(OnlineGameIntent.SubmitGuess(it)) },
            onLeave = {
                viewModel.eventHandler(OnlineGameIntent.Disconnect)
                onBack()
            },
            modifier = modifier.fillMaxSize(),
        )

        OnlineScreen.Result -> OnlineResultScreen(
            state = state.gameState,
            localPlayer = state.localPlayer,
            onAcceptResult = { viewModel.eventHandler(OnlineGameIntent.AcceptResult) },
            modifier = modifier.fillMaxSize(),
        )
    }
}
