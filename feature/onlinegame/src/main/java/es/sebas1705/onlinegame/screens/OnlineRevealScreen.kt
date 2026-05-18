package es.sebas1705.onlinegame.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import es.sebas1705.models.OnlineGameState

@Composable
internal fun OnlineRevealScreen(
    state: OnlineGameState,
    onMarkRevealDone: () -> Unit,
    onLeave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var cardVisible by rememberSaveable { mutableStateOf(false) }

    val confirmedCount = state.confirmedRevealIds.size
    val totalCount = state.players.size
    val alreadyConfirmed = state.players.any {
        // Current player already confirmed if their id is in confirmedRevealIds.
        // The local player is the one whose word we received, identified by the transport.
        // We approximate by checking if confirmedCount == totalCount (all done)
        false
    }

    Scaffold(modifier = modifier) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Your Role",
                style = MaterialTheme.typography.headlineSmall,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Only look when you are alone — don't show the screen to others.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Role card ─────────────────────────────────────────────────
            Card(
                onClick = { cardVisible = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = if (state.isImpostor)
                        MaterialTheme.colorScheme.errorContainer
                    else
                        MaterialTheme.colorScheme.primaryContainer,
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    if (!cardVisible) {
                        Icon(
                            imageVector = Icons.Outlined.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Tap to reveal your role",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = if (state.isImpostor)
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (state.isImpostor) "IMPOSTOR" else "CIVILIAN",
                            style = MaterialTheme.typography.displaySmall,
                            color = if (state.isImpostor)
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        if (state.isImpostor) {
                            Text(
                                text = "Try to guess the word without being caught!",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                            )
                            if (state.impostorsKnowEachOther && state.impostorPlayerIndexes.size > 1) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Your allies: " + state.impostorPlayerIndexes
                                        .mapNotNull { state.players.getOrNull(it)?.name }
                                        .filter { name -> name != state.players.firstOrNull()?.name }
                                        .joinToString(", "),
                                    style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                )
                            }
                        } else {
                            val word = state.word
                            if (word != null) {
                                Text(
                                    text = "Word:",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                                Text(
                                    text = word,
                                    style = MaterialTheme.typography.displayMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center,
                                )
                            }
                            if (state.clues.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Clues: ${state.clues.joinToString(" · ")}",
                                    style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                            }
                        }
                        if (state.showNumOfImpostors) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Impostors in game: ${state.impostorCount}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Progress: waiting for others ──────────────────────────────
            Text(
                text = "$confirmedCount / $totalCount players confirmed",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (totalCount > 0) {
                LinearProgressIndicator(
                    progress = { confirmedCount.toFloat() / totalCount },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Action buttons ────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick = onLeave,
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Leave")
                }
                Button(
                    onClick = onMarkRevealDone,
                    enabled = cardVisible,
                    modifier = Modifier.weight(2f),
                ) {
                    if (confirmedCount < totalCount) {
                        Text("I've seen it — waiting for others")
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Text("All confirmed — starting…")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
