package es.sebas1705.onlinegame.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.SportsScore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import es.sebas1705.models.OnlineGameResult
import es.sebas1705.models.OnlineGameState
import es.sebas1705.models.OnlinePlayer
import es.sebas1705.models.OnlineWinner

@Composable
internal fun OnlineResultScreen(
    state: OnlineGameState,
    localPlayer: OnlinePlayer?,
    onAcceptResult: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val result = state.result
    val acceptedCount = state.acceptedResultIds.size
    val totalCount = state.players.size
    val alreadyAccepted = localPlayer?.id?.let { it in state.acceptedResultIds } == true

    Scaffold(modifier = modifier) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // ── Winner banner ──────────────────────────────────────────────
            if (result != null) {
                val winnersAreImpostors = result.winner == OnlineWinner.Impostors
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (winnersAreImpostors)
                            MaterialTheme.colorScheme.errorContainer
                        else
                            MaterialTheme.colorScheme.primaryContainer,
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(
                            imageVector = if (winnersAreImpostors) Icons.Outlined.SportsScore
                            else Icons.Outlined.EmojiEvents,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = if (winnersAreImpostors) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = if (winnersAreImpostors) "Impostors Win!" else "Civilians Win!",
                            style = MaterialTheme.typography.headlineMedium,
                            color = if (winnersAreImpostors) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                        )
                        if (result.reason.isNotBlank()) {
                            Text(
                                text = result.reason,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = if (winnersAreImpostors) MaterialTheme.colorScheme.onErrorContainer
                                else MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                        if (result.word.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "The word was: ${result.word}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Player list ──────────────────────────────────────────
                Text(
                    text = "Players",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Start),
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    items(state.players, key = { it.id }) { player ->
                        val isImpostor = state.impostorPlayerIndexes.contains(state.players.indexOf(player))
                        PlayerResultRow(
                            player = player,
                            isImpostor = isImpostor,
                            isLocal = player.id == localPlayer?.id,
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Waiting for result…",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Return to room progress ────────────────────────────────────
            Text(
                text = "$acceptedCount / $totalCount players ready to continue",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (totalCount > 0) {
                LinearProgressIndicator(
                    progress = { acceptedCount.toFloat() / totalCount },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onAcceptResult,
                enabled = !alreadyAccepted,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = if (alreadyAccepted) "Waiting for others…" else "Accept & return to room"
                )
            }
        }
    }
}

@Composable
private fun PlayerResultRow(
    player: OnlinePlayer,
    isImpostor: Boolean,
    isLocal: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isImpostor -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = if (isImpostor) Icons.Outlined.SportsScore else Icons.Outlined.Shield,
                contentDescription = null,
                tint = if (isImpostor) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = player.name + if (isLocal) " (You)" else "",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isLocal) androidx.compose.ui.text.font.FontWeight.Bold else null,
                )
                Text(
                    text = if (isImpostor) "Impostor" else "Civilian",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isImpostor) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.primary,
                )
            }
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
