@file:Suppress("LongMethod")

package es.sebas1705.offlinegame.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.sebas1705.core.resources.R
import es.sebas1705.feature.offlinegame.BuildConfig
import es.sebas1705.offlinegame.models.screenLogD
import es.sebas1705.offlinegame.ui.components.DebugVotesSummary
import es.sebas1705.offlinegame.ui.components.DiscussionFeedbackCard
import es.sebas1705.offlinegame.ui.components.DiscussionStartPlayerHeader
import es.sebas1705.offlinegame.ui.components.GuessWordButton
import es.sebas1705.offlinegame.ui.components.GuessWordInput
import es.sebas1705.offlinegame.ui.components.VotePlayerItem
import es.sebas1705.offlinegame.ui.components.VoteSectionHeader
import es.sebas1705.offlinegame.viewmodel.OfflineGameState

@Composable
internal fun OfflineGameDiscussionScreen(
    uiState: OfflineGameState,
    onVotePlayer: (Int) -> Unit,
    onGuess: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var guessValue by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(uiState.alivePlayerIndexes.size, uiState.guessFeedback) {
        screenLogD(
            "discussion phase alive=${uiState.alivePlayerIndexes.size} " +
                "feedback=${uiState.guessFeedback.orEmpty()}"
        )
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(contentType = "contentType1") {
            DiscussionStartPlayerHeader(
                speakerName = uiState.players.getOrElse(uiState.currentSpeakerIndex) {
                    stringResource(R.string.core_resources_unknown)
                }
            )
        }

        if (!uiState.guessFeedback.isNullOrBlank()) {
            item(contentType = "contentType2") {
                DiscussionFeedbackCard(uiState.guessFeedback)
            }
        }

        item(contentType = "contentType3") {
            GuessWordInput(
                value = guessValue,
                onValueChange = { guessValue = it }
            )
        }

        item(contentType = "contentType4") {
            GuessWordButton(onClick = { onGuess(guessValue) })
        }

        item(contentType = "contentType5") {
            VoteSectionHeader()
        }

        itemsIndexed(
            items = uiState.players,
            key = { index, _ -> index },
            contentType = { _, _ -> "contentType6" }
        ) { index, playerName ->
            val isAlive = index in uiState.alivePlayerIndexes
            val isImpostor = index in uiState.impostorPlayerIndexes
            VotePlayerItem(
                index = index,
                playerName = playerName,
                isAlive = isAlive,
                isImpostor = isImpostor,
                onVotePlayer = onVotePlayer
            )
        }

        if (BuildConfig.DEBUG) {
            item(contentType = "contentType7") {
                DebugVotesSummary(
                    correctVotes = uiState.correctVotes,
                    incorrectVotes = uiState.incorrectVotes
                )
            }
        }
    }
}
