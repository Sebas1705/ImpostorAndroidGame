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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import es.sebas1705.models.GameRoom
import es.sebas1705.models.NetworkMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OnlineLobbyScreen(
    rooms: List<GameRoom>,
    isLoading: Boolean,
    selectedMode: NetworkMode,
    errorMessage: String?,
    onCreateRoom: (playerName: String, maxPlayers: Int) -> Unit,
    onJoinRoom: (room: GameRoom, playerName: String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    savedNickname: String = "",
) {
    var showCreateSheet by rememberSaveable { mutableStateOf(false) }
    var playerName by rememberSaveable(savedNickname) { mutableStateOf(savedNickname) }
    var joiningRoom by rememberSaveable { mutableStateOf<GameRoom?>(null) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateSheet = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Create room")
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (selectedMode == NetworkMode.Local) "Local Rooms" else "Online Rooms",
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Text(
                        text = if (selectedMode == NetworkMode.Local)
                            "Rooms found on your WiFi network"
                        else
                            "Rooms available on the internet",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                OutlinedButton(onClick = onBack) { Text("Back") }
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (rooms.isEmpty()) {
                Text(
                    text = "No rooms found. Tap + to create one.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(rooms, key = { it.id }) { room ->
                        RoomCard(
                            room = room,
                            onJoin = { joiningRoom = room },
                        )
                    }
                }
            }
        }
    }

    // ── Create room sheet ──────────────────────────────────────────────────
    if (showCreateSheet) {
        ModalBottomSheet(
            onDismissRequest = { showCreateSheet = false },
            sheetState = sheetState,
        ) {
            CreateRoomSheet(
                playerName = playerName,
                onPlayerNameChange = { playerName = it },
                onCreate = { name, max ->
                    showCreateSheet = false
                    onCreateRoom(name, max)
                },
                onDismiss = { showCreateSheet = false },
            )
        }
    }

    // ── Join room sheet ────────────────────────────────────────────────────
    joiningRoom?.let { room ->
        ModalBottomSheet(
            onDismissRequest = { joiningRoom = null },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        ) {
            JoinRoomSheet(
                room = room,
                playerName = playerName,
                onPlayerNameChange = { playerName = it },
                onJoin = { name ->
                    joiningRoom = null
                    onJoinRoom(room, name)
                },
                onDismiss = { joiningRoom = null },
            )
        }
    }
}

@Composable
private fun RoomCard(
    room: GameRoom,
    onJoin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = room.hostName, style = MaterialTheme.typography.titleMedium)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "${room.playerCount}/${room.maxPlayers}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Button(
                onClick = onJoin,
                enabled = room.playerCount < room.maxPlayers,
            ) {
                Text("Join")
            }
        }
    }
}

@Composable
private fun CreateRoomSheet(
    playerName: String,
    onPlayerNameChange: (String) -> Unit,
    onCreate: (name: String, maxPlayers: Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var maxPlayers by rememberSaveable { mutableStateOf("8") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(text = "Create Room", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = playerName,
            onValueChange = onPlayerNameChange,
            label = { Text("Your name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = maxPlayers,
            onValueChange = { maxPlayers = it.filter { c -> c.isDigit() } },
            label = { Text("Max players (2-12)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        ) {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
            Button(
                onClick = {
                    val max = maxPlayers.toIntOrNull()?.coerceIn(2, 12) ?: 8
                    onCreate(playerName.trim(), max)
                },
                enabled = playerName.isNotBlank(),
            ) {
                Text("Create")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun JoinRoomSheet(
    room: GameRoom,
    playerName: String,
    onPlayerNameChange: (String) -> Unit,
    onJoin: (name: String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(text = "Join ${room.hostName}'s room", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = playerName,
            onValueChange = onPlayerNameChange,
            label = { Text("Your name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        ) {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
            Button(
                onClick = { onJoin(playerName.trim()) },
                enabled = playerName.isNotBlank(),
            ) {
                Text("Join")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
