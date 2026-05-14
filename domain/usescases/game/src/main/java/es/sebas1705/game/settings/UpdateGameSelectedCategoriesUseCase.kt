package es.sebas1705.game.settings

import es.sebas1705.models.Categories
import es.sebas1705.repositories.interfaces.IGameSettingsRepository
import javax.inject.Inject

class UpdateGameSelectedCategoriesUseCase @Inject constructor(
    private val gameRepository: IGameSettingsRepository
) {
    suspend operator fun invoke(
        selectedCategories: Set<Categories>
    ) = gameRepository.updateSelectedCategories(
        selectedCategories.map { it.name }.toSet()
    )
}

