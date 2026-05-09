package es.sebas1705.main

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import es.sebas1705.common.utlis.extensions.primitives.pushAndFree
import es.sebas1705.main.debug.DebugToolsScreen
import es.sebas1705.main.viewmodel.MainIntent
import es.sebas1705.main.viewmodel.MainViewModel
import es.sebas1705.mvisample.MviSampleScreen
import es.sebas1705.mvvmsample.MvvmSampleScreen
import es.sebas1705.splash.SplashScreen
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator

/**
 * Navigation for the app.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@Composable
fun AppNav(
    modifier: Modifier = Modifier,
    isDebugBuild: Boolean = false,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val backStack = rememberNavBackStack(AppGraph.SplashScreen)
    val mainState by mainViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(null) {
        mainViewModel.eventHandler(MainIntent.ChargeData)
    }
    LaunchedEffect(mainState.splashFinished) {
        if (mainState.splashFinished) {
            backStack.pushAndFree(AppGraph.MVISampleScreen)
        }
    }
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = {
            ContentTransform(
                slideInHorizontally(initialOffsetX = { it }),
                ExitTransition.None
            )
        },
        popTransitionSpec = {
            ContentTransform(
                slideInHorizontally(initialOffsetX = { it }),
                ExitTransition.None
            )
        },
        entryProvider = entryProvider {
            entry<AppGraph.SplashScreen> {
                SplashScreen()
            }
            entry<AppGraph.MvvmSampleScreen> {
                MvvmSampleScreen(
                    onMVINav = { backStack.pushAndFree(AppGraph.MVISampleScreen) },
                    showDebugActions = isDebugBuild,
                    onDebugNav = {
                        if (isDebugBuild) {
                            backStack.pushAndFree(AppGraph.DebugToolsScreen)
                        }
                    }
                )
            }
            entry<AppGraph.MVISampleScreen> {
                MviSampleScreen { backStack.pushAndFree(AppGraph.MvvmSampleScreen) }
            }
            entry<AppGraph.DebugToolsScreen> {
                DebugToolsScreen(onBack = { backStack.pushAndFree(AppGraph.MvvmSampleScreen) })
            }
        }
    )
}

