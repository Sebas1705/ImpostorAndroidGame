package es.sebas1705.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.sebas1705.core.resources.R
import es.sebas1705.models.AppLanguage
import es.sebas1705.settings.models.AppSettingsDialogActions
import es.sebas1705.settings.viewmodel.AppSettingsUiState


@Composable
internal fun GameplaySettingsCard(
    uiState: AppSettingsUiState,
    actions: AppSettingsDialogActions
) {
    val settings = uiState.settings ?: return

    SettingCard(title = stringResource(R.string.core_resources_settings_gameplay_title)) {
        Text(
            text = stringResource(R.string.core_resources_settings_language),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AppLanguage.entries.forEach { language ->
                FilterChip(
                    selected = settings.appLanguage == language,
                    onClick = { actions.onUpdateLanguage(language) },
                    label = {
                        Text(
                            text = when (language) {
                                AppLanguage.English -> stringResource(R.string.core_resources_language_english)
                                AppLanguage.Spanish -> stringResource(R.string.core_resources_language_spanish)
                            }
                        )
                    }
                )
            }
        }
    }
}