package es.sebas1705.offlinegame.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material.icons.outlined.TheaterComedy
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.sebas1705.core.resources.R
import es.sebas1705.feature.offlinegame.BuildConfig

private const val TIMER_WARNING_THRESHOLD = 30

@Composable
internal fun DiscussionTimer(secondsLeft: Int, totalSeconds: Int) {
    val isWarning = secondsLeft <= TIMER_WARNING_THRESHOLD
    val progress = secondsLeft.toFloat() / totalSeconds.toFloat()
    val minutes = secondsLeft / 60
    val seconds = secondsLeft % 60
    val timeText = "%d:%02d".format(minutes, seconds)

    val trackColor by animateColorAsState(
        targetValue = if (isWarning) MaterialTheme.colorScheme.errorContainer
        else MaterialTheme.colorScheme.surfaceContainerHigh,
        animationSpec = tween(durationMillis = 600),
        label = "timer_track_color"
    )
    val indicatorColor by animateColorAsState(
        targetValue = if (isWarning) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 600),
        label = "timer_indicator_color"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = trackColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isWarning) {
                        stringResource(R.string.core_resources_discussion_timer_warning)
                    } else {
                        stringResource(R.string.core_resources_discussion_timer_label)
                    },
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = timeText,
                    style = MaterialTheme.typography.titleLarge,
                    color = indicatorColor
                )
            }
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = indicatorColor,
                trackColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun DiscussionSessionChips(
    totalPlayers: Int,
    alivePlayers: Int,
    impostorCount: Int
) {
    val eliminated = totalPlayers - alivePlayers
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        SuggestionChip(
            onClick = {},
            label = { Text(stringResource(R.string.core_resources_info_alive, alivePlayers)) },
            icon = {
                Icon(
                    Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(SuggestionChipDefaults.IconSize),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
        if (eliminated > 0) {
            SuggestionChip(
                onClick = {},
                label = { Text(stringResource(R.string.core_resources_info_eliminated, eliminated)) },
                icon = {
                    Icon(
                        Icons.Outlined.RemoveCircleOutline,
                        contentDescription = null,
                        modifier = Modifier.size(SuggestionChipDefaults.IconSize),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            )
        }
        SuggestionChip(
            onClick = {},
            label = { Text(stringResource(R.string.core_resources_info_impostors, impostorCount)) },
            icon = {
                Icon(
                    Icons.Outlined.TheaterComedy,
                    contentDescription = null,
                    modifier = Modifier.size(SuggestionChipDefaults.IconSize)
                )
            }
        )
    }
}

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
internal fun GuessWordInput(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.core_resources_guess_word_label)) },
        enabled = enabled
    )
}

@Composable
internal fun GuessWordButton(
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
    ) {
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
    val cardColor by animateColorAsState(
        targetValue = if (isAlive) MaterialTheme.colorScheme.surfaceContainerLow
        else MaterialTheme.colorScheme.surfaceContainerLowest,
        animationSpec = tween(300),
        label = "vote_card_color_$index"
    )
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = playerName,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isAlive) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            text = if (isAlive) stringResource(R.string.core_resources_alive)
                            else stringResource(R.string.core_resources_already_voted),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = if (isAlive) Icons.Outlined.CheckCircle
                            else Icons.Outlined.RemoveCircleOutline,
                            contentDescription = null,
                            modifier = Modifier.size(AssistChipDefaults.IconSize),
                            tint = if (isAlive) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.error
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (isAlive)
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                        else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
                    )
                )
                if (BuildConfig.DEBUG) {
                    Text(
                        text = if (isImpostor) stringResource(R.string.core_resources_debug_impostor)
                        else stringResource(R.string.core_resources_debug_civilian),
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

