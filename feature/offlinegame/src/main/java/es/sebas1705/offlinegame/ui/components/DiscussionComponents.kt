package es.sebas1705.offlinegame.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.sebas1705.core.resources.R
import es.sebas1705.feature.offlinegame.BuildConfig

@Composable
internal fun DiscussionStartPlayerHeader(speakerName: String) {
    Text(
        text = stringResource(R.string.core_resources_starts_player, speakerName),
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
internal fun DiscussionFeedbackCard(feedback: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Text(
            text = feedback,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
internal fun GuessWordInput(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.core_resources_guess_word_label)) }
    )
}

@Composable
internal fun GuessWordButton(onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.core_resources_guess_word_button))
    }
}

@Composable
internal fun VoteSectionHeader() {
    Text(
        text = stringResource(R.string.core_resources_vote_players),
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
internal fun VotePlayerItem(
    index: Int,
    playerName: String,
    isAlive: Boolean,
    isImpostor: Boolean,
    onVotePlayer: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
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
                if (BuildConfig.DEBUG) {
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
                enabled = isAlive,
                modifier = Modifier.testTag("vote_player_$index")
            ) {
                Text(stringResource(R.string.core_resources_vote))
            }
        }
    }
}

@Composable
internal fun DebugVotesSummary(correctVotes: Int, incorrectVotes: Int) {
    Text(
        text = stringResource(
            R.string.core_resources_votes_summary,
            correctVotes,
            incorrectVotes
        ),
        style = MaterialTheme.typography.bodyMedium
    )
}

