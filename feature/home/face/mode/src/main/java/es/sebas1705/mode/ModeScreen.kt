package es.sebas1705.mode

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import es.sebas1705.mode.design.ModeDesign
import es.sebas1705.mode.viewmodel.ModeIntent
import es.sebas1705.mode.viewmodel.ModeViewModel
import es.sebas1705.models.Modes

@Composable
fun ModeScreen(
    mode: Modes,
    impostors: Int,
    showImpostorsInResult: Boolean,
    discussionTimerSeconds: Int,
    impostorsKnowEachOther: Boolean,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    modeViewModel: ModeViewModel = hiltViewModel()
) {
    ModeDesign(
        modifier = modifier,
        mode = mode,
        impostors = impostors,
        showImpostorsInResult = showImpostorsInResult,
        discussionTimerSeconds = discussionTimerSeconds,
        impostorsKnowEachOther = impostorsKnowEachOther,
        onSave = { selectedMode, selectedImpostors, selectedShowImpostorsInResult, selectedTimer, selectedKnow ->
            modeViewModel.eventHandler(
                ModeIntent.Save(
                    mode = selectedMode,
                    impostors = selectedImpostors,
                    showImpostorsInResult = selectedShowImpostorsInResult,
                    discussionTimerSeconds = selectedTimer,
                    impostorsKnowEachOther = selectedKnow,
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
    mode: Modes,
    impostors: Int,
    showImpostorsInResult: Boolean,
    discussionTimerSeconds: Int,
    impostorsKnowEachOther: Boolean,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        ModeScreen(
            mode = mode,
            impostors = impostors,
            showImpostorsInResult = showImpostorsInResult,
            discussionTimerSeconds = discussionTimerSeconds,
            impostorsKnowEachOther = impostorsKnowEachOther,
            modifier = Modifier,
            onBack = onDismiss,
        )
    }
}
