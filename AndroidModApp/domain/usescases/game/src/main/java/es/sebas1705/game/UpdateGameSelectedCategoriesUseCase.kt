package es.sebas1705.game

import es.sebas1705.models.Categories
import es.sebas1705.repositories.interfaces.IGameRepository
import javax.inject.Inject

class UpdateGameSelectedCategoriesUseCase @Inject constructor(
    private val gameRepository: IGameRepository
) {
    suspend operator fun invoke(
        selectedCategories: Set<Categories>
    ) = gameRepository.updateSelectedCategories(
        selectedCategories.map { it.name }.toSet()
    )
}

