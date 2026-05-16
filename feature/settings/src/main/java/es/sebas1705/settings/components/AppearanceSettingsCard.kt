package es.sebas1705.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.sebas1705.core.resources.R
import es.sebas1705.models.DarkThemePreference
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

        Text(
            text = stringResource(R.string.core_resources_settings_dark_theme),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            DarkThemePreference.entries.forEach { pref ->
                val isSelected = pref == settings.darkThemePreference
                AssistChip(
                    onClick = { actions.onUpdateDarkTheme(pref) },
                    label = {
                        val labelRes = when (pref) {
                            DarkThemePreference.System -> R.string.core_resources_settings_theme_system
                            DarkThemePreference.Light -> R.string.core_resources_settings_theme_light
                            DarkThemePreference.Dark -> R.string.core_resources_settings_theme_dark
                        }
                        Text(stringResource(labelRes))
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceContainerHigh
                        }
                    )
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
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
