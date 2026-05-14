@file:Suppress("LongMethod")

package es.sebas1705.offlinegame.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import es.sebas1705.offlinegame.models.screenLogD
import es.sebas1705.offlinegame.ui.components.RevealCard
import es.sebas1705.offlinegame.ui.components.RevealContinueButton
import es.sebas1705.offlinegame.ui.components.RevealHeader
import es.sebas1705.offlinegame.viewmodel.OfflineGameState

private const val REVEALED_OFFSET_PX = -280f

@Composable
internal fun OfflineGameRevealScreen(
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
        screenLogD(
            "reveal phase playerIndex=$playerIndex player=${uiState.currentPlayerName} " +
                "revealed=${uiState.revealedCurrentCard}"
        )
        dragOffset = if (uiState.revealedCurrentCard) REVEALED_OFFSET_PX else 0f
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        RevealHeader(
            playerIndex = playerIndex,
            playersSize = uiState.players.size,
            currentPlayerName = uiState.currentPlayerName
        )

        RevealCard(
            uiState = uiState,
            isImpostor = isImpostor,
            clue = clue,
            word = wordEntry?.word.orEmpty(),
            dragOffset = dragOffset,
            rotationY = rotationY,
            density = density.density,
            onDragOffsetChange = { dragOffset = it },
            onRevealDone = onRevealDone
        )

        RevealContinueButton(
            hasMoreRevealPlayers = uiState.hasMoreRevealPlayers,
            revealedCurrentCard = uiState.revealedCurrentCard,
            onNextRevealPlayer = onNextRevealPlayer
        )
    }
}
