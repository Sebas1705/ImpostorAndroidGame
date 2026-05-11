package es.sebas1705.settings.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import es.sebas1705.core.resources.R
import es.sebas1705.settings.models.SettingsQuickActionsState

@Composable
internal fun SettingsQuickActionsCard(
    state: SettingsQuickActionsState,
    onOpenCategories: () -> Unit,
    onOpenDebugTools: () -> Unit,
    onResetDefaults: () -> Unit,
    onSignOut: () -> Unit
) {
    SettingCard(title = stringResource(R.string.core_resources_settings_quick_actions)) {
        FilledTonalButton(
            onClick = onOpenCategories,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.AutoMirrored.Outlined.LibraryBooks, contentDescription = null)
            Text(stringResource(R.string.core_resources_settings_open_categories))
        }

        if (state.isDebugBuild) {
            FilledTonalButton(
                onClick = onOpenDebugTools,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Outlined.BugReport, contentDescription = null)
                Text(stringResource(R.string.core_resources_settings_open_debug_tools))
            }
        }

        OutlinedButton(
            onClick = onResetDefaults,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Outlined.Settings, contentDescription = null)
            Text(stringResource(R.string.core_resources_settings_reset_defaults))
        }

        Button(
            onClick = onSignOut,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isSignOutLoading
        ) {
            Icon(Icons.AutoMirrored.Outlined.Logout, contentDescription = null)
            Text(
                if (state.isSignOutLoading) {
                    stringResource(R.string.core_resources_settings_signing_out)
                } else {
                    stringResource(R.string.core_resources_settings_sign_out)
                }
            )
        }
    }
}

