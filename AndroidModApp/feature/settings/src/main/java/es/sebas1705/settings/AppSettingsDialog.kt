package es.sebas1705.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.sebas1705.settings.design.AppSettingsDialogDesign
import es.sebas1705.settings.models.AppSettingsDialogActions

@Composable
fun AppSettingsDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onOpenCategories: () -> Unit,
    onOpenDebugTools: () -> Unit,
    onSignedOut: () -> Unit,
    viewModel: AppSettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navigateToLogin) {
        if (uiState.navigateToLogin) {
            viewModel.consumeSignOutNavigation()
            onSignedOut()
        }
    }

    AppSettingsDialogDesign(
        modifier = modifier,
        uiState = uiState,
        actions = AppSettingsDialogActions(
            onDismiss = onDismiss,
            onOpenCategories = onOpenCategories,
            onOpenDebugTools = onOpenDebugTools,
            onUpdateMusicVolume = viewModel::updateMusicVolume,
            onUpdateSoundVolume = viewModel::updateSoundVolume,
            onUpdateContrast = viewModel::updateContrast,
            onUpdateLanguage = viewModel::updateLanguage,
            onSetShowTutorialOnNextStart = viewModel::setShowTutorialOnNextStart,
            onResetDefaults = viewModel::resetDefaults,
            onSignOut = viewModel::signOut
        )
    )
}




