package es.sebas1705.categories.viewmodel

import es.sebas1705.common.mvi.MVIBaseIntent
import es.sebas1705.models.Categories

sealed interface CategoriesIntent : MVIBaseIntent {
    data class ToggleCategory(
        val categoriesStates: Map<Categories, Boolean>,
        val category: Categories
    ) : CategoriesIntent
    data object SelectAll : CategoriesIntent
    data object ClearSelection : CategoriesIntent
}

