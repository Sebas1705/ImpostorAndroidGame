package es.sebas1705.home.nav.design

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.sebas1705.home.nav.viewmodel.FaceIntent
import es.sebas1705.home.nav.viewmodel.FaceState
import es.sebas1705.home.nav.viewmodel.FaceViewModel

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

