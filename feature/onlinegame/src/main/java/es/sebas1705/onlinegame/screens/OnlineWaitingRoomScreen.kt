package es.sebas1705.onlinegame.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.TimerOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.sebas1705.models.Categories
import es.sebas1705.models.Modes
import es.sebas1705.models.OnlinePlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OnlineWaitingRoomScreen(
    connectedPlayers: List<OnlinePlayer>,
    isHost: Boolean,
    // current config
    categories: Set<Categories>,
    mode: Modes,
    impostors: Int,
    discussionTimerSeconds: Int,
    impostorsKnowEachOther: Boolean,
    showNumOfImpostors: Boolean,
    // callbacks
    onUpdateConfig: (
        categories: Set<Categories>,
        mode: Modes,
        impostors: Int,
        discussionTimerSeconds: Int,
        impostorsKnowEachOther: Boolean,
        showNumOfImpostors: Boolean,
    ) -> Unit,
    onStartGame: () -> Unit,
    onLeave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showConfigSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(modifier = modifier) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
        ) {
            // ── Header ─────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Waiting Room", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        text = "${connectedPlayers.size} player(s) connected",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (isHost) {
                    IconButton(onClick = { showConfigSheet = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit game settings",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Config summary card ────────────────────────────────────────
            ConfigSummaryCard(
                categories = categories,
                mode = mode,
                impostors = impostors,
                discussionTimerSeconds = discussionTimerSeconds,
                impostorsKnowEachOther = impostorsKnowEachOther,
                showNumOfImpostors = showNumOfImpostors,
                connectedCount = connectedPlayers.size,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Player list ────────────────────────────────────────────────
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

            // ── Start / wait ───────────────────────────────────────────────
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
                    text = "Waiting for the host to start…",
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

    // ── Config bottom sheet (host only) ────────────────────────────────────
    if (showConfigSheet) {
        ModalBottomSheet(
            onDismissRequest = { showConfigSheet = false },
            sheetState = sheetState,
        ) {
            GameConfigSheet(
                initialCategories = categories,
                initialMode = mode,
                initialImpostors = impostors,
                initialTimer = discussionTimerSeconds,
                initialImpostorsKnow = impostorsKnowEachOther,
                initialShowNumImpostors = showNumOfImpostors,
                connectedCount = connectedPlayers.size,
                onSave = { cats, m, imp, timer, know, showNum ->
                    onUpdateConfig(cats, m, imp, timer, know, showNum)
                    showConfigSheet = false
                },
                onDismiss = { showConfigSheet = false },
            )
        }
    }
}

// ── Config summary card ────────────────────────────────────────────────────

@Composable
private fun ConfigSummaryCard(
    categories: Set<Categories>,
    mode: Modes,
    impostors: Int,
    discussionTimerSeconds: Int,
    impostorsKnowEachOther: Boolean,
    showNumOfImpostors: Boolean,
    connectedCount: Int,
    modifier: Modifier = Modifier,
) {
    val effectiveImpostors = impostors.coerceAtMost(maxOf(1, connectedCount - 1))
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = "Game Settings",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
            ConfigRow(
                icon = { Icon(Icons.Outlined.Category, null, modifier = Modifier.size(16.dp)) },
                label = "${categories.size} categor${if (categories.size == 1) "y" else "ies"} selected",
            )
            ConfigRow(
                icon = { Icon(Icons.Outlined.Settings, null, modifier = Modifier.size(16.dp)) },
                label = "${mode.name} · $effectiveImpostors impostor${if (effectiveImpostors != 1) "s" else ""}" +
                    if (impostors != effectiveImpostors) " (adjusted from $impostors)" else "",
            )
            ConfigRow(
                icon = {
                    Icon(
                        if (discussionTimerSeconds <= 0) Icons.Outlined.TimerOff else Icons.Outlined.Timer,
                        null,
                        modifier = Modifier.size(16.dp),
                    )
                },
                label = if (discussionTimerSeconds <= 0) "No time limit"
                else "$discussionTimerSeconds s per round",
            )
            if (impostorsKnowEachOther) {
                ConfigRow(
                    icon = { Icon(Icons.Outlined.Groups, null, modifier = Modifier.size(16.dp)) },
                    label = "Impostors know each other",
                )
            }
            if (showNumOfImpostors) {
                ConfigRow(
                    icon = { Icon(Icons.Outlined.Groups, null, modifier = Modifier.size(16.dp)) },
                    label = "Impostor count shown to all",
                )
            }
        }
    }
}

@Composable
private fun ConfigRow(
    icon: @Composable () -> Unit,
    label: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        icon()
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ── Game config sheet ──────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GameConfigSheet(
    initialCategories: Set<Categories>,
    initialMode: Modes,
    initialImpostors: Int,
    initialTimer: Int,
    initialImpostorsKnow: Boolean,
    initialShowNumImpostors: Boolean,
    connectedCount: Int,
    onSave: (
        categories: Set<Categories>,
        mode: Modes,
        impostors: Int,
        timer: Int,
        impostorsKnow: Boolean,
        showNumImpostors: Boolean,
    ) -> Unit,
    onDismiss: () -> Unit,
) {
    // local editable copies
    var selectedCategories by rememberSaveable { mutableStateOf(initialCategories) }
    var selectedMode by rememberSaveable { mutableStateOf(initialMode) }
    var impostors by rememberSaveable { mutableIntStateOf(initialImpostors) }
    var timerSeconds by rememberSaveable { mutableIntStateOf(initialTimer) }
    var impostorsKnow by rememberSaveable { mutableStateOf(initialImpostorsKnow) }
    var showNumImpostors by rememberSaveable { mutableStateOf(initialShowNumImpostors) }

    val maxImpostors = maxOf(1, connectedCount - 1).coerceAtLeast(10)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(text = "Game Settings", style = MaterialTheme.typography.titleLarge)

        // ── Impostors ──────────────────────────────────────────────────────
        SectionTitle("Impostors")
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OutlinedButton(
                onClick = { if (impostors > 1) impostors-- },
                enabled = impostors > 1,
                modifier = Modifier.size(40.dp),
                contentPadding = PaddingValues(0.dp),
            ) { Text("−") }
            Text(
                text = impostors.toString(),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.width(48.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
            OutlinedButton(
                onClick = { impostors++ },
                modifier = Modifier.size(40.dp),
                contentPadding = PaddingValues(0.dp),
            ) { Text("+") }
            Text(
                text = if (connectedCount >= 2)
                    "(max $maxImpostors with ${connectedCount}p)"
                else
                    "(adjusted at start)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
        }

        HorizontalDivider()

        // ── Mode ───────────────────────────────────────────────────────────
        SectionTitle("Game Mode")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Modes.entries.forEach { m ->
                FilterChip(
                    selected = selectedMode == m,
                    onClick = { selectedMode = m },
                    label = { Text(m.name) },
                )
            }
        }

        HorizontalDivider()

        // ── Discussion timer ───────────────────────────────────────────────
        SectionTitle("Discussion Timer")
        val timerPresets = listOf(0, 60, 90, 120, 180, 300)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            timerPresets.forEach { preset ->
                FilterChip(
                    selected = timerSeconds == preset,
                    onClick = { timerSeconds = preset },
                    label = {
                        Text(if (preset == 0) "No limit" else "${preset}s")
                    },
                )
            }
        }

        HorizontalDivider()

        // ── Toggles ────────────────────────────────────────────────────────
        SectionTitle("Rules")
        ToggleRow(
            label = "Impostors know each other",
            description = "Impostors can see who else is an impostor.",
            checked = impostorsKnow,
            onCheckedChange = { impostorsKnow = it },
        )
        ToggleRow(
            label = "Show impostor count",
            description = "All players see how many impostors are in the game.",
            checked = showNumImpostors,
            onCheckedChange = { showNumImpostors = it },
        )

        HorizontalDivider()

        // ── Categories ─────────────────────────────────────────────────────
        SectionTitle("Categories (${selectedCategories.size} selected)")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Categories.entries.forEach { cat ->
                FilterChip(
                    selected = cat in selectedCategories,
                    onClick = {
                        selectedCategories = if (cat in selectedCategories)
                            selectedCategories - cat
                        else
                            selectedCategories + cat
                    },
                    label = { Text(cat.displayName) },
                )
            }
        }

        // ── Save / cancel ──────────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        ) {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
            Button(
                onClick = {
                    onSave(
                        selectedCategories,
                        selectedMode,
                        impostors,
                        timerSeconds,
                        impostorsKnow,
                        showNumImpostors,
                    )
                },
                enabled = selectedCategories.isNotEmpty(),
            ) { Text("Save") }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
private fun ToggleRow(
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
