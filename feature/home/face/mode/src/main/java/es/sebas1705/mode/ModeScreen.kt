package es.sebas1705.mode

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import es.sebas1705.common.FaceState
import es.sebas1705.mode.design.ModeDesign
import es.sebas1705.mode.viewmodel.ModeIntent
import es.sebas1705.mode.viewmodel.ModeViewModel
import es.sebas1705.models.Modes
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ModeScreen(
    faceState: FaceState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    modeViewModel: ModeViewModel = hiltViewModel()
) {
    ModeDesign(
        modifier = modifier,
        faceState = faceState,
        onSave = { selectedMode, selectedImpostors, selectedShowImpostorsInResult, selectedTimer, selectedKnow, selectedShowNumOfImpostors ->
            modeViewModel.eventHandler(
                ModeIntent.Save(
                    mode = selectedMode,
                    impostors = selectedImpostors,
                    showImpostorsInResult = selectedShowImpostorsInResult,
                    discussionTimerSeconds = selectedTimer,
                    impostorsKnowEachOther = selectedKnow,
                    showNumOfImpostors = selectedShowNumOfImpostors
                )
            )
            onBack()
        },
        onBack = onBack
    )
}

@Composable
fun ModeFullScreenDialog(
    onDismiss: () -> Unit,
    faceState: FaceState,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        ModeScreen(
            faceState = faceState,
            modifier = Modifier,
            onBack = onDismiss,
        )
    }
}
