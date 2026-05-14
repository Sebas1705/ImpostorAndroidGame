package es.sebas1705.categories.viewmodel

import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.common.mvi.MVIBaseViewModel
import es.sebas1705.game.settings.UpdateGameSelectedCategoriesUseCase
import es.sebas1705.models.Categories
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val updateGameSelectedCategoriesUseCase: UpdateGameSelectedCategoriesUseCase
) : MVIBaseViewModel<CategoriesState, CategoriesIntent>(context) {

    override fun initState(): CategoriesState = CategoriesState()

    override fun intentHandler(intent: CategoriesIntent) =
        when (intent) {
            is CategoriesIntent.ToggleCategory -> toggleCategory(intent)
            CategoriesIntent.SelectAll -> selectAll()
            CategoriesIntent.ClearSelection -> clearAll()
        }

    private fun toggleCategory(
        intent: CategoriesIntent.ToggleCategory
    ) = execute(Dispatchers.IO) {
        updateGameSelectedCategoriesUseCase(
            selectedCategories = intent.categoriesStates
                .mapValues { (category, isSelected) ->
                    if (category == intent.category) !isSelected else isSelected
                }
                .filterValues { it }
                .keys
                .toSet()
        )
    }

    private fun selectAll() = execute(Dispatchers.IO) {
        updateGameSelectedCategoriesUseCase(
            selectedCategories = Categories.entries.toSet()
        )
    }

    private fun clearAll() = execute(Dispatchers.IO) {
        updateGameSelectedCategoriesUseCase(
            selectedCategories = emptySet()
        )
    }
}

