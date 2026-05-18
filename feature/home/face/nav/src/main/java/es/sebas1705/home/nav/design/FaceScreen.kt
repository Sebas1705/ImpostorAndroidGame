package es.sebas1705.home.nav.design

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import es.sebas1705.common.FaceState

@Composable
fun FaceScreen(
    modifier: Modifier = Modifier,
    faceState: FaceState = FaceState(),
    isLoading: Boolean = false,
    onOpenUser: () -> Unit = {},
    onOpenCategories: () -> Unit = {},
    onOpenMode: () -> Unit = {},
    onStartOfflineGame: () -> Unit = {},
    onStartOnlineGame: (networkMode: String) -> Unit = {},
    onOpenSettings: () -> Unit = {},
) {
    FaceDesign(
        modifier = modifier,
        faceState = faceState,
        isLoading = isLoading,
        onOpenUser = onOpenUser,
        onOpenCategories = onOpenCategories,
        onOpenMode = onOpenMode,
        onStartOfflineGame = onStartOfflineGame,
        onStartOnlineGame = onStartOnlineGame,
        onOpenSettings = onOpenSettings,
    )
}

