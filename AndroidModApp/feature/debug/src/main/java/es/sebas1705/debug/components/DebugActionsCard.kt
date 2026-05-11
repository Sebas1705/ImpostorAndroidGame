package es.sebas1705.debug.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.sebas1705.core.resources.R as ResourceR

@Composable
internal fun DebugActionsCard(
    onImportDefaultWords: () -> Unit,
    onRefresh: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = stringResource(ResourceR.string.core_resources_debug_actions_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Button(
                onClick = onImportDefaultWords,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Outlined.BugReport, contentDescription = null)
                Text(stringResource(ResourceR.string.core_resources_debug_import_default_words))
            }
            FilledTonalButton(
                onClick = onRefresh,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(ResourceR.string.core_resources_debug_refresh_diagnostics))
            }
        }
    }
}

