package es.sebas1705.main.components

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import es.sebas1705.common.utlis.extensions.primitives.pop
import es.sebas1705.common.utlis.extensions.primitives.push
import es.sebas1705.common.utlis.extensions.primitives.pushAndFree
import es.sebas1705.debug.DebugToolsScreen
import es.sebas1705.home.nav.HomeNav
import es.sebas1705.login.LoginScreen
import es.sebas1705.main.AppGraph
import es.sebas1705.splash.SplashScreen

private const val NAV_ANIMATION_DURATION_MS = 280

private fun forwardTransition() = ContentTransform(
    targetContentEnter = fadeIn(
        animationSpec = tween(
            durationMillis = NAV_ANIMATION_DURATION_MS,
            easing = FastOutSlowInEasing
        )
    ) + slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth / 4 },
        animationSpec = tween(
            durationMillis = NAV_ANIMATION_DURATION_MS,
            easing = FastOutSlowInEasing
        )
    ),
    initialContentExit = fadeOut(
        animationSpec = tween(durationMillis = NAV_ANIMATION_DURATION_MS / 2)
    ) + slideOutHorizontally(
        targetOffsetX = { fullWidth -> -(fullWidth / 10) },
        animationSpec = tween(durationMillis = NAV_ANIMATION_DURATION_MS)
    )
)

private fun backwardTransition() = ContentTransform(
    targetContentEnter = fadeIn(
        animationSpec = tween(
            durationMillis = NAV_ANIMATION_DURATION_MS,
            easing = FastOutSlowInEasing
        )
    ) + slideInHorizontally(
        initialOffsetX = { fullWidth -> -(fullWidth / 4) },
        animationSpec = tween(
            durationMillis = NAV_ANIMATION_DURATION_MS,
            easing = FastOutSlowInEasing
        )
    ),
    initialContentExit = fadeOut(
        animationSpec = tween(durationMillis = NAV_ANIMATION_DURATION_MS / 2)
    ) + slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth / 10 },
        animationSpec = tween(durationMillis = NAV_ANIMATION_DURATION_MS)
    )
)

@Composable
internal fun AppNavigationContent(
    backStack: NavBackStack<NavKey>,
    onOpenSettings: () -> Unit,
) {
    NavDisplay(
        modifier = Modifier.fillMaxSize(),
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = { forwardTransition() },
        popTransitionSpec = { backwardTransition() },
        entryProvider = entryProvider {
            entry<AppGraph.SplashScreen> {
                SplashScreen()
            }
            entry<AppGraph.LoginScreen> {
                LoginScreen(
                    onLoginSuccess = { backStack.pushAndFree(AppGraph.HomeScreen) }
                )
            }
            entry<AppGraph.HomeScreen> {
                HomeNav(
                    modifier = Modifier.fillMaxSize(),
                    onSignOut = { backStack.pushAndFree(AppGraph.LoginScreen) },
                    onDebugNav = { backStack.push(AppGraph.DebugToolsScreen) },
                    onOpenSettings = onOpenSettings
                )
            }
            entry<AppGraph.DebugToolsScreen> {
                DebugToolsScreen(onBack = { backStack.pop() })
            }
        }
    )
}


