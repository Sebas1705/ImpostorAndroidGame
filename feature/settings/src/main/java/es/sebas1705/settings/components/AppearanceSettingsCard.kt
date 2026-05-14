package es.sebas1705.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import es.sebas1705.core.resources.R
import es.sebas1705.settings.models.AppSettingsDialogActions
import es.sebas1705.settings.viewmodel.AppSettingsUiState

@Composable
internal fun AppearanceSettingsCard(
    uiState: AppSettingsUiState,
    actions: AppSettingsDialogActions
) {
    val settings = uiState.settings ?: return

    SettingCard(title = stringResource(R.string.core_resources_settings_appearance_title)) {
        Text(
            text = stringResource(R.string.core_resources_settings_ui_contrast),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        ContrastChips(
            selected = settings.appContrast,
            onSelect = actions.onUpdateContrast
        )

        Row(
            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.core_resources_settings_compact_tables),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Switch(
                checked = settings.forceCompactTables,
                onCheckedChange = actions.onUpdateCompactTables
            )
        }
    }
}