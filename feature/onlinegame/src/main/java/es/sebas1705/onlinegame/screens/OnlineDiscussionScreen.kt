package es.sebas1705.onlinegame.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.HowToVote
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import es.sebas1705.models.ChatMessage
import es.sebas1705.models.OnlineGameState
import es.sebas1705.models.OnlinePlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OnlineDiscussionScreen(
    state: OnlineGameState,
    localPlayer: OnlinePlayer?,
    isHost: Boolean,
    onSendChatMessage: (String) -> Unit,
    onVotePlayer: (Int) -> Unit,
    onSubmitGuess: (String) -> Unit,
    onLeave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var messageText by rememberSaveable { mutableStateOf("") }
    var showVoteDialog by rememberSaveable { mutableStateOf(false) }
    var showGuessDialog by rememberSaveable { mutableStateOf(false) }
    var guessText by rememberSaveable { mutableStateOf("") }

    val listState = rememberLazyListState()
    val isMyTurn = state.players.getOrNull(state.currentSpeakerIndex)?.id == localPlayer?.id
    val isImpostor = state.isImpostor

    LaunchedEffect(state.chatMessages.size) {
        if (state.chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(state.chatMessages.size - 1)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Discussion",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = "${state.currentPlayerName}'s turn",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isMyTurn) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                actions = {
                    if (isHost) {
                        IconButton(onClick = { showVoteDialog = true }) {
                            Icon(
                                imageVector = Icons.Outlined.HowToVote,
                                contentDescription = "Vote to eliminate",
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            ) {
                if (isMyTurn) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        OutlinedTextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Say something…") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(
                                onSend = {
                                    if (messageText.isNotBlank()) {
                                        onSendChatMessage(messageText.trim())
                                        messageText = ""
                                    }
                                }
                            ),
                        )
                        IconButton(
                            onClick = {
                                if (messageText.isNotBlank()) {
                                    onSendChatMessage(messageText.trim())
                                    messageText = ""
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.Send,
                                contentDescription = "Send",
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                    if (isImpostor) {
                        Spacer(modifier = Modifier.height(4.dp))
                        TextButton(
                            onClick = { showGuessDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = "Guess the word instead",
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Waiting for ${state.currentPlayerName} to speak…",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            items(state.chatMessages, key = { it.timestamp }) { message ->
                ChatBubble(
                    message = message,
                    isMine = message.playerId == localPlayer?.id,
                )
            }
        }
    }

    // ── Vote dialog (host only) ────────────────────────────────────────────
    if (showVoteDialog) {
        AlertDialog(
            onDismissRequest = { showVoteDialog = false },
            title = { Text("Vote to eliminate") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.alivePlayerIndexes
                        .filter { i -> state.players.getOrNull(i)?.id != localPlayer?.id }
                        .forEach { i ->
                            val player = state.players.getOrNull(i) ?: return@forEach
                            Button(
                                onClick = {
                                    onVotePlayer(i)
                                    showVoteDialog = false
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(player.name)
                            }
                        }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showVoteDialog = false }) { Text("Cancel") }
            },
        )
    }

    // ── Guess dialog (impostors only) ────────────────────────────────────
    if (showGuessDialog) {
        AlertDialog(
            onDismissRequest = { showGuessDialog = false },
            title = { Text("Guess the word") },
            text = {
                OutlinedTextField(
                    value = guessText,
                    onValueChange = { guessText = it },
                    label = { Text("Your guess") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (guessText.isNotBlank()) {
                            onSubmitGuess(guessText.trim())
                            guessText = ""
                            showGuessDialog = false
                        }
                    },
                    enabled = guessText.isNotBlank(),
                ) { Text("Submit") }
            },
            dismissButton = {
                TextButton(onClick = { showGuessDialog = false }) { Text("Cancel") }
            },
        )
    }

    // ── Guess feedback snackbar ───────────────────────────────────────────
    val feedback = state.guessFeedback
    if (feedback != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                ),
            ) {
                Text(
                    text = feedback,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }
        }
    }
}

@Composable
private fun ChatBubble(
    message: ChatMessage,
    isMine: Boolean,
    modifier: Modifier = Modifier,
) {
    val alignment = if (isMine) Alignment.End else Alignment.Start
    val bubbleColor = if (isMine)
        MaterialTheme.colorScheme.primaryContainer
    else
        MaterialTheme.colorScheme.surfaceVariant

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = alignment,
    ) {
        if (!isMine) {
            Text(
                text = message.playerName,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(2.dp))
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start,
        ) {
            if (!isMine) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .align(Alignment.Top)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = bubbleColor),
            ) {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
