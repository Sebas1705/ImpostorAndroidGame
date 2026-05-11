package es.sebas1705.settings.design

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import es.sebas1705.core.resources.R
import es.sebas1705.feature.settings.BuildConfig
import es.sebas1705.settings.AppSettingsUiState
import es.sebas1705.settings.components.ContrastChips
import es.sebas1705.settings.components.SettingCard
import es.sebas1705.settings.components.SettingsQuickActionsCard
import es.sebas1705.settings.models.AppSettingsDialogActions
import es.sebas1705.settings.models.SettingsQuickActionsState
import es.sebas1705.models.AppLanguage
import kotlin.math.roundToInt

@Composable
private fun SettingsLoadingContent(
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.padding(start = 8.dp))
    }
}

@Composable
private fun AudioSettingsCard(
    uiState: AppSettingsUiState,
    actions: AppSettingsDialogActions
) {
    val settings = uiState.settings ?: return

    SettingCard(title = stringResource(R.string.core_resources_settings_audio_title)) {
        Text(
            stringResource(
                R.string.core_resources_settings_music_percent,
                (settings.musicVolume * 100f).roundToInt()
            )
        )
        Slider(
            value = settings.musicVolume,
            onValueChange = actions.onUpdateMusicVolume,
            valueRange = 0f..1f
        )
        Text(
            stringResource(
                R.string.core_resources_settings_effects_percent,
                (settings.soundVolume * 100f).roundToInt()
            )
        )
        Slider(
            value = settings.soundVolume,
            onValueChange = actions.onUpdateSoundVolume,
            valueRange = 0f..1f
        )
    }
}

@Composable
private fun AppearanceSettingsCard(
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
    }
}

@Composable
private fun GameplaySettingsCard(
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.core_resources_settings_show_tutorial))
                Text(
                    text = stringResource(R.string.core_resources_settings_show_tutorial_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = settings.firstTime,
                onCheckedChange = actions.onSetShowTutorialOnNextStart
            )
        }
    }
}

@Composable
private fun SettingsLoadedContent(
    paddingValues: PaddingValues,
    uiState: AppSettingsUiState,
    actions: AppSettingsDialogActions
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(contentType = "contentType1") {
            AudioSettingsCard(uiState = uiState, actions = actions)
        }
        item(contentType = "contentType2") {
            AppearanceSettingsCard(uiState = uiState, actions = actions)
        }
        item(contentType = "contentType3") {
            GameplaySettingsCard(uiState = uiState, actions = actions)
        }
        item(contentType = "contentType4") {
            SettingsQuickActionsCard(
                state = SettingsQuickActionsState(
                    isDebugBuild = BuildConfig.DEBUG,
                    isSignOutLoading = uiState.isSignOutLoading
                ),
                onOpenCategories = actions.onOpenCategories,
                onOpenDebugTools = actions.onOpenDebugTools,
                onResetDefaults = actions.onResetDefaults,
                onSignOut = actions.onSignOut
            )
        }
        if (!uiState.errorMessage.isNullOrBlank()) {
            item(contentType = "contentType5") {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun AppSettingsDialogDesign(
    uiState: AppSettingsUiState,
    actions: AppSettingsDialogActions,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = actions.onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.core_resources_settings_title)) },
                    navigationIcon = {
                        IconButton(onClick = actions.onDismiss) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = stringResource(R.string.core_resources_close)
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            if (uiState.isLoading || uiState.settings == null) {
                SettingsLoadingContent(paddingValues)
            } else {
                SettingsLoadedContent(
                    paddingValues = paddingValues,
                    uiState = uiState,
                    actions = actions
                )
            }
        }
    }
}


