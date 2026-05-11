package es.sebas1705.categories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.sebas1705.categories.design.CategoriesDesign
import es.sebas1705.categories.viewmodel.CategoriesIntent
import es.sebas1705.categories.viewmodel.CategoriesViewModel
import es.sebas1705.models.Categories
import kotlinx.collections.immutable.ImmutableMap

@Composable
fun CategoriesScreen(
    categoriesStates: ImmutableMap<Categories, Boolean>,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    categoriesViewModel: CategoriesViewModel = hiltViewModel()
) {
    CategoriesDesign(
        modifier = modifier,
        categoriesStates = categoriesStates,
        onCategoryClick = { category ->
            categoriesViewModel.eventHandler(CategoriesIntent.ToggleCategory(categoriesStates, category))
        },
        onSelectAll = {
            categoriesViewModel.eventHandler(CategoriesIntent.SelectAll)
        },
        onClearSelection = {
            categoriesViewModel.eventHandler(CategoriesIntent.ClearSelection)
        },
        onBack = onBack
    )
}

@Composable
fun CategoriesFullScreenDialog(
    onDismiss: () -> Unit,
    categoriesStates: ImmutableMap<Categories, Boolean>,
    categoriesViewModel: CategoriesViewModel = hiltViewModel(),
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        CategoriesScreen(
            categoriesStates = categoriesStates,
            modifier = Modifier,
            onBack = onDismiss,
            categoriesViewModel = categoriesViewModel
        )
    }
}


