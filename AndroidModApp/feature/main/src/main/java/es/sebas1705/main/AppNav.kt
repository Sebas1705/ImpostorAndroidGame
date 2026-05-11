package es.sebas1705.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.core.os.LocaleListCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.appcompat.app.AppCompatDelegate
import es.sebas1705.common.utlis.extensions.primitives.push
import es.sebas1705.common.utlis.extensions.primitives.pushAndFree
import es.sebas1705.main.design.AppNavDesign
import es.sebas1705.main.models.AppNavOverlayActions
import es.sebas1705.main.models.AppNavOverlayState
import es.sebas1705.main.viewmodel.MainIntent
import es.sebas1705.main.viewmodel.MainViewModel

/**
 * Navigation for the app.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@Composable
@Suppress("LongMethod")
fun AppNav(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val backStack = rememberNavBackStack(AppGraph.SplashScreen)
    val mainState by mainViewModel.uiState.collectAsStateWithLifecycle()
    val currentDestination = backStack.lastOrNull() as? AppGraph
    val showSettingsDialog = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(null) {
        mainViewModel.eventHandler(MainIntent.ChargeData)
    }
    LaunchedEffect(mainState.splashFinished) {
        if (mainState.splashFinished) {
            backStack.pushAndFree(
                if (mainState.isUserLogged) AppGraph.HomeScreen
                else AppGraph.LoginScreen
            )
        }
    }

    LaunchedEffect(mainState.appLanguage) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(mainState.appLanguage.code)
        )
    }

    AppNavDesign(
        modifier = modifier,
        backStack = backStack,
        overlayState = AppNavOverlayState(
            currentDestination = currentDestination,
            showSettingsDialog = showSettingsDialog.value
        ),
        overlayActions = AppNavOverlayActions(
            onOpenSettings = { showSettingsDialog.value = true },
            onDismissSettings = { showSettingsDialog.value = false },
            onOpenCategories = {
                showSettingsDialog.value = false
                backStack.pushAndFree(AppGraph.HomeScreen)
            },
            onOpenDebugTools = {
                showSettingsDialog.value = false
                backStack.push(AppGraph.DebugToolsScreen)
            },
            onSignedOut = {
                showSettingsDialog.value = false
                backStack.pushAndFree(AppGraph.LoginScreen)
            }
        )
    )
}
