package es.sebas1705.game

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import es.sebas1705.game.design.UserDesign
import es.sebas1705.game.viewmodel.UserIntent
import es.sebas1705.game.viewmodel.UserViewModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun UserScreen(
    users: ImmutableList<String>,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    userViewModel: UserViewModel = hiltViewModel(),
) {
    UserDesign(
        modifier = modifier,
        playerNames = users,
        onSave = { userViewModel.eventHandler(UserIntent.Save(it)) },
        onBack = onBack
    )
}

@Composable
fun UserFullScreenDialog(
    users: ImmutableList<String>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        UserScreen(
            users = users,
            modifier = modifier,
            onBack = onDismiss,
        )
    }
}

