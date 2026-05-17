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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import es.sebas1705.models.OnlinePlayer

@Composable
internal fun OnlineWaitingRoomScreen(
    connectedPlayers: List<OnlinePlayer>,
    isHost: Boolean,
    onStartGame: () -> Unit,
    onLeave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Text(text = "Waiting Room", style = MaterialTheme.typography.headlineSmall)
        Text(
            text = "${connectedPlayers.size} player(s) connected",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            contentPadding = PaddingValues(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f),
        ) {
            items(connectedPlayers, key = { it.id }) { player ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = if (player.isHost) Icons.Default.Star else Icons.Default.Person,
                        contentDescription = null,
                        tint = if (player.isHost) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = player.name + if (player.isHost) " (Host)" else "",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isHost) {
            Button(
                onClick = onStartGame,
                enabled = connectedPlayers.size >= 2,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Start Game")
            }
            if (connectedPlayers.size < 2) {
                Text(
                    text = "Need at least 2 players to start",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }
        } else {
            Text(
                text = "Waiting for the host to start...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onLeave,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Leave")
        }
    }
}
