@file:Suppress("LongMethod")

package es.sebas1705.home.nav.offline

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.sebas1705.core.resources.R
import es.sebas1705.feature.home.face.nav.BuildConfig
import es.sebas1705.home.nav.offline.viewmodel.OfflineGameIntent
import es.sebas1705.home.nav.offline.viewmodel.OfflineGameResult
import es.sebas1705.home.nav.offline.viewmodel.OfflineGameState
import es.sebas1705.home.nav.offline.viewmodel.OfflineGameStep
import es.sebas1705.home.nav.offline.viewmodel.OfflineGameViewModel
import es.sebas1705.home.nav.offline.viewmodel.OfflineWinner
import es.sebas1705.models.Categories
import es.sebas1705.models.Modes
import kotlin.math.roundToInt

private const val REVEAL_THRESHOLD_PX = -180f
private const val REVEALED_OFFSET_PX = -280f

@Composable
fun OfflineGameScreen(
    players: List<String>,
    categories: Set<Categories>,
    mode: Modes,
    impostors: Int,
    showImpostorsInResult: Boolean,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    isDebugBuild: Boolean = BuildConfig.DEBUG,
    viewModel: OfflineGameViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.eventHandler(
            OfflineGameIntent.Initialize(
                players = players,
                categories = categories,
                mode = mode,
                impostors = impostors,
                isDebugBuild = isDebugBuild
            )
        )
    }

    OfflineGameDesign(
        modifier = modifier,
        uiState = uiState,
        onBack = onBack,
        onRevealDone = { viewModel.eventHandler(OfflineGameIntent.MarkRevealDone) },
        onNextRevealPlayer = { viewModel.eventHandler(OfflineGameIntent.NextRevealPlayer) },
        onVotePlayer = { index -> viewModel.eventHandler(OfflineGameIntent.VotePlayer(index)) },
        onGuess = { value -> viewModel.eventHandler(OfflineGameIntent.SubmitGuess(value)) },
        isDebugBuild = isDebugBuild,
        showImpostorsInResult = showImpostorsInResult
    )
}

@Composable
fun OfflineGameFullScreenDialog(
    players: List<String>,
    categories: Set<Categories>,
    mode: Modes,
    impostors: Int,
    showImpostorsInResult: Boolean,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        OfflineGameScreen(
            players = players,
            categories = categories,
            mode = mode,
            impostors = impostors,
            showImpostorsInResult = showImpostorsInResult,
            onBack = onDismiss
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OfflineGameDesign(
    uiState: OfflineGameState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onRevealDone: () -> Unit,
    onNextRevealPlayer: () -> Unit,
    onVotePlayer: (Int) -> Unit,
    onGuess: (String) -> Unit,
    isDebugBuild: Boolean,
    showImpostorsInResult: Boolean
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.core_resources_offline_game_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.core_resources_back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        if (!uiState.errorMessage.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
            return@Scaffold
        }

        when (uiState.step) {
            OfflineGameStep.Reveal -> {
                RevealPhase(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    uiState = uiState,
                    onRevealDone = onRevealDone,
                    onNextRevealPlayer = onNextRevealPlayer
                )
            }

            OfflineGameStep.Discussion -> {
                DiscussionPhase(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    uiState = uiState,
                    onVotePlayer = onVotePlayer,
                    onGuess = onGuess,
                    isDebugBuild = isDebugBuild
                )
            }

            OfflineGameStep.Result -> {
                ResultPhase(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    result = uiState.result,
                    showImpostorsInResult = showImpostorsInResult,
                    onBack = onBack
                )
            }
        }
    }
}

@Composable
private fun RevealPhase(
    uiState: OfflineGameState,
    modifier: Modifier = Modifier,
    onRevealDone: () -> Unit,
    onNextRevealPlayer: () -> Unit
) {
    val playerIndex = uiState.currentRevealIndex
    val isImpostor = playerIndex in uiState.impostorPlayerIndexes
    val wordEntry = uiState.wordEntry
    val clue = wordEntry?.clue
        ?.getOrNull(playerIndex % (wordEntry.clue.size.coerceAtLeast(1)))
        .orEmpty()

    var dragOffset by remember(playerIndex) { mutableFloatStateOf(0f) }
    val rotationY by animateFloatAsState(
        targetValue = if (uiState.revealedCurrentCard) 180f else 0f,
        label = "reveal_card_rotation"
    )
    val density = LocalDensity.current
    LaunchedEffect(uiState.revealedCurrentCard, playerIndex) {
        dragOffset = if (uiState.revealedCurrentCard) REVEALED_OFFSET_PX else 0f
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(
                R.string.core_resources_player_progress,
                playerIndex + 1,
                uiState.players.size,
                uiState.currentPlayerName
            ),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.core_resources_swipe_card_reveal),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .offset { IntOffset(0, dragOffset.roundToInt()) }
                    .graphicsLayer {
                        this.rotationY = rotationY
                        cameraDistance = 16f * density.density
                    }
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState { delta ->
                            if (!uiState.revealedCurrentCard) {
                                dragOffset = (dragOffset + delta).coerceIn(REVEALED_OFFSET_PX, 0f)
                            }
                        },
                        onDragStopped = {
                            if (dragOffset <= REVEAL_THRESHOLD_PX) {
                                dragOffset = REVEALED_OFFSET_PX
                                onRevealDone()
                            } else {
                                dragOffset = 0f
                            }
                        }
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            // Counter-rotate back content so text isn't mirrored after card flip.
                            if (rotationY > 90f) {
                                this.rotationY = 180f
                            }
                        }
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!uiState.revealedCurrentCard) {
                        Text(
                            text = uiState.currentPlayerName,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = stringResource(R.string.core_resources_swipe_up_reveal),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    } else {
                        Text(
                            text = if (isImpostor) {
                                stringResource(R.string.core_resources_role_impostor)
                            } else {
                                stringResource(R.string.core_resources_role_civilian)
                            },
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = if (isImpostor) {
                                stringResource(R.string.core_resources_clue_prefix, clue)
                            } else {
                                stringResource(
                                    R.string.core_resources_word_prefix,
                                    wordEntry?.word.orEmpty()
                                )
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        FilledTonalButton(
            onClick = onNextRevealPlayer,
            enabled = uiState.revealedCurrentCard,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (uiState.hasMoreRevealPlayers) {
                    stringResource(R.string.core_resources_next_player)
                } else {
                    stringResource(R.string.core_resources_start_game)
                }
            )
        }
    }
}

@Composable
private fun DiscussionPhase(
    uiState: OfflineGameState,
    modifier: Modifier = Modifier,
    onVotePlayer: (Int) -> Unit,
    onGuess: (String) -> Unit,
    isDebugBuild: Boolean
) {
    var guessValue by rememberSaveable { mutableStateOf("") }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = stringResource(
                    R.string.core_resources_starts_player,
                    uiState.players.getOrElse(uiState.currentSpeakerIndex) {
                        stringResource(R.string.core_resources_unknown)
                    }
                ),
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (!uiState.guessFeedback.isNullOrBlank()) {
            item {
                Text(
                    text = uiState.guessFeedback,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        item {
            OutlinedTextField(
                value = guessValue,
                onValueChange = { guessValue = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.core_resources_guess_word_label)) }
            )
        }

        item {
            OutlinedButton(
                onClick = { onGuess(guessValue) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.core_resources_guess_word_button))
            }
        }

        item {
            Text(
                text = stringResource(R.string.core_resources_vote_players),
                style = MaterialTheme.typography.titleMedium
            )
        }

        itemsIndexed(uiState.players, key = { index, _ -> index }) { index, playerName ->
            val isAlive = index in uiState.alivePlayerIndexes
            val isImpostor = index in uiState.impostorPlayerIndexes

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(playerName, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = if (isAlive) {
                                stringResource(R.string.core_resources_alive)
                            } else {
                                stringResource(R.string.core_resources_already_voted)
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (isDebugBuild) {
                            Text(
                                text = if (isImpostor) {
                                    stringResource(R.string.core_resources_debug_impostor)
                                } else {
                                    stringResource(R.string.core_resources_debug_civilian)
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                    Button(
                        onClick = { onVotePlayer(index) },
                        enabled = if (isDebugBuild) {
                            isAlive && isImpostor
                        } else {
                            isAlive
                        },
                        modifier = Modifier.testTag("vote_player_$index")
                    ) {
                        Text(stringResource(R.string.core_resources_vote))
                    }
                }
            }
        }

        if (isDebugBuild) {
            item {
                Text(
                    text = stringResource(
                        R.string.core_resources_votes_summary,
                        uiState.correctVotes,
                        uiState.incorrectVotes
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun ResultPhase(
    result: OfflineGameResult?,
    showImpostorsInResult: Boolean,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (result == null) {
            Text(stringResource(R.string.core_resources_no_result_available))
            FilledTonalButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.core_resources_back))
            }
            return
        }

        Text(
            text = when (result.winner) {
                OfflineWinner.Civilians -> stringResource(R.string.core_resources_result_civilians_win)
                OfflineWinner.Impostors -> stringResource(R.string.core_resources_result_impostors_win)
                OfflineWinner.Tie -> stringResource(R.string.core_resources_result_tie)
            },
            style = MaterialTheme.typography.headlineSmall
        )
        Text(result.reason)
        Text(stringResource(R.string.core_resources_result_secret_word, result.word))
        Text(
            if (showImpostorsInResult) {
                stringResource(
                    R.string.core_resources_result_impostors,
                    result.impostorNames.joinToString()
                )
            } else {
                stringResource(R.string.core_resources_result_impostors_hidden_by_mode)
            }
        )
        Text(stringResource(R.string.core_resources_votes_summary, result.correctVotes, result.incorrectVotes))

        Spacer(modifier = Modifier.height(8.dp))

        FilledTonalButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.core_resources_close))
        }
    }
}


