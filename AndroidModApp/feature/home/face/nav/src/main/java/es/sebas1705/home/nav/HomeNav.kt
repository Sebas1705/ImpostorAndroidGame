package es.sebas1705.home.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.rememberNavBackStack
import es.sebas1705.home.nav.design.HomeDesign

@Composable
@Suppress("LongMethod")
fun HomeNav(
    modifier: Modifier = Modifier,
    onSignOut: () -> Unit = {},
    onDebugNav: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
) {
    val backStack = rememberNavBackStack(HomeGraph.FaceScreen)

    HomeDesign(
        modifier = modifier,
        backStack = backStack,
        onSignOut = onSignOut,
        onDebugNav = onDebugNav,
        onOpenSettings = onOpenSettings
    )
}

