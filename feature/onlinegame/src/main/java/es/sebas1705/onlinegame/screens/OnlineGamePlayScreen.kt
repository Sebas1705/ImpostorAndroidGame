package es.sebas1705.onlinegame.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import es.sebas1705.models.OnlineGameState
import es.sebas1705.models.OnlineGameStep
import es.sebas1705.models.OnlineWinner

@Composable
internal fun OnlineGamePlayScreen(
    state: OnlineGameState,
    isHost: Boolean,
    onMarkRevealDone: () -> Unit,
    onNextRevealPlayer: () -> Unit,
    onVotePlayer: (Int) -> Unit,
    onSubmitGuess: (String) -> Unit,
    onLeave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state.step) {
        OnlineGameStep.Lobby -> Unit
        OnlineGameStep.Reveal -> RevealPhase(
            state = state,
            isHost = isHost,
            onMarkRevealDone = onMarkRevealDone,
            onNextRevealPlayer = onNextRevealPlayer,
            modifier = modifier,
        )
        OnlineGameStep.Discussion -> DiscussionPhase(
            state = state,
            isHost = isHost,
            onVotePlayer = onVotePlayer,
            onSubmitGuess = onSubmitGuess,
            modifier = modifier,
        )
        OnlineGameStep.Result -> ResultPhase(
            state = state,
            onLeave = onLeave,
            modifier = modifier,
        )
    }
}

@Composable
private fun RevealPhase(
    state: OnlineGameState,
    isHost: Boolean,
    onMarkRevealDone: () -> Unit,
    onNextRevealPlayer: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Reveal Phase",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${state.currentPlayerName}'s turn",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(32.dp))

        if (!state.revealedCurrentCard) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (state.isImpostor) {
                        Text(
                            text = "IMPOSTOR",
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.error,
                        )
                        state.clues.firstOrNull()?.let { clue ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Clue: $clue", style = MaterialTheme.typography.bodyLarge)
                        }
                    } else {
                        Text(text = "Your word:", style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.word ?: "—",
                            style = MaterialTheme.typography.displaySmall,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onMarkRevealDone, modifier = Modifier.fillMaxWidth()) {
                Text("Done — pass the phone")
            }
        } else if (isHost) {
            Text(
                text = "Player has seen their card.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onNextRevealPlayer, modifier = Modifier.fillMaxWidth()) {
                Text(
                    if (state.hasMoreRevealPlayers) "Next player" else "Start discussion"
                )
            }
        } else {
            Text(
                text = "Waiting for host to advance...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun DiscussionPhase(
    state: OnlineGameState,
    isHost: Boolean,
    onVotePlayer: (Int) -> Unit,
    onSubmitGuess: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var guess by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(text = "Discussion", style = MaterialTheme.typography.headlineMedium)
        Text(
            text = "First speaker: ${state.players.getOrNull(state.currentSpeakerIndex)?.name ?: "?"}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        state.guessFeedback?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.secondary)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Vote to eliminate:", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f),
        ) {
            itemsIndexed(state.players) { index, player ->
                val isAlive = index in state.alivePlayerIndexes
                OutlinedButton(
                    onClick = { onVotePlayer(index) },
                    enabled = isAlive && isHost,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = player.name + if (!isAlive) " (eliminated)" else "",
                        color = if (!isAlive) MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }

        if (state.isImpostor) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Guess the word:", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = guess,
                    onValueChange = { guess = it },
                    label = { Text("Your guess") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = {
                        onSubmitGuess(guess)
                        guess = ""
                    }),
                    modifier = Modifier.weight(1f),
                )
                Button(
                    onClick = {
                        onSubmitGuess(guess)
                        guess = ""
                    },
                    enabled = guess.isNotBlank(),
                ) {
                    Text("Guess")
                }
            }
        }
    }
}

@Composable
private fun ResultPhase(
    state: OnlineGameState,
    onLeave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val result = state.result ?: return
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = when (result.winner) {
                OnlineWinner.Civilians -> "Civilians win!"
                OnlineWinner.Impostors -> "Impostors win!"
                OnlineWinner.Tie -> "It's a tie!"
            },
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = result.reason, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Word: ${result.word}", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "Impostors: ${result.impostorNames.joinToString()}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onLeave, modifier = Modifier.fillMaxWidth()) {
            Text("Back to lobby")
        }
    }
}
