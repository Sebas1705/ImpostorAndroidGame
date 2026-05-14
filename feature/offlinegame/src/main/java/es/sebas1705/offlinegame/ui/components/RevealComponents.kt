package es.sebas1705.offlinegame.ui.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import es.sebas1705.core.resources.R
import es.sebas1705.offlinegame.viewmodel.OfflineGameState
import kotlin.math.roundToInt

private const val REVEAL_THRESHOLD_PX = -180f
private const val REVEALED_OFFSET_PX = -280f

@Composable
internal fun RevealHeader(playerIndex: Int, playersSize: Int, currentPlayerName: String) {
    Text(
        text = stringResource(
            R.string.core_resources_player_progress,
            playerIndex + 1,
            playersSize,
            currentPlayerName
        ),
        style = MaterialTheme.typography.titleLarge
    )
    Text(
        text = stringResource(R.string.core_resources_swipe_card_reveal),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
internal fun RevealCard(
    uiState: OfflineGameState,
    isImpostor: Boolean,
    clue: String,
    word: String,
    dragOffset: Float,
    rotationY: Float,
    density: Float,
    onDragOffsetChange: (Float) -> Unit,
    onRevealDone: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .offset { IntOffset(0, dragOffset.roundToInt()) }
                .graphicsLayer {
                    this.rotationY = rotationY
                    cameraDistance = 16f * density
                }
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        if (!uiState.revealedCurrentCard) {
                            onDragOffsetChange((dragOffset + delta).coerceIn(REVEALED_OFFSET_PX, 0f))
                        }
                    },
                    onDragStopped = {
                        if (dragOffset <= REVEAL_THRESHOLD_PX) {
                            onDragOffsetChange(REVEALED_OFFSET_PX)
                            onRevealDone()
                        } else {
                            onDragOffsetChange(0f)
                        }
                    }
                ),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            RevealCardContent(
                uiState = uiState,
                isImpostor = isImpostor,
                clue = clue,
                word = word,
                rotationY = rotationY
            )
        }
    }
}

@Composable
private fun RevealCardContent(
    uiState: OfflineGameState,
    isImpostor: Boolean,
    clue: String,
    word: String,
    rotationY: Float
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                if (rotationY > 90f) this.rotationY = 180f
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
                    stringResource(R.string.core_resources_word_prefix, word)
                },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
internal fun RevealContinueButton(
    hasMoreRevealPlayers: Boolean,
    revealedCurrentCard: Boolean,
    onNextRevealPlayer: () -> Unit
) {
    FilledTonalButton(
        onClick = onNextRevealPlayer,
        enabled = revealedCurrentCard,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            if (hasMoreRevealPlayers) {
                stringResource(R.string.core_resources_next_player)
            } else {
                stringResource(R.string.core_resources_start_game)
            }
        )
    }
}

