package es.sebas1705.settings

import es.sebas1705.models.Categories
import es.sebas1705.repositories.interfaces.IGameSettingsRepository
import javax.inject.Inject

@Deprecated("Use UpdateGameSelectedCategoriesUseCase from domain:usescases:game")
class UpdateSelectedCategoriesUseCase @Inject constructor(
    private val gameRepository: IGameSettingsRepository
) {
    /**
     * Updates the selected categories in the settings
     *
     * @param selectedCategories [Set]<[Categories]> The new set of selected categories to update
     *
     * @since 0.1.0
     * @author Sebas1705 05/10/2026
     */
    suspend operator fun invoke(
        selectedCategories: Set<Categories>
    ) = gameRepository.updateSelectedCategories(
        selectedCategories.map { it.name }.toSet()
    )
}