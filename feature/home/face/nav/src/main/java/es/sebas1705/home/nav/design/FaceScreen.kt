package es.sebas1705.home.nav.design

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import es.sebas1705.home.nav.viewmodel.FaceState

@Composable
fun FaceScreen(
    modifier: Modifier = Modifier,
    faceState: FaceState = FaceState(),
    onOpenUser: () -> Unit = {},
    onOpenCategories: () -> Unit = {},
    onOpenMode: () -> Unit = {},
    onStartOfflineGame: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
) {
    FaceDesign(
        modifier = modifier,
        faceState = faceState,
        onOpenUser = onOpenUser,
        onOpenCategories = onOpenCategories,
        onOpenMode = onOpenMode,
        onStartOfflineGame = onStartOfflineGame,
        onOpenSettings = onOpenSettings
    )
}

