package es.sebas1705.mappers

import es.sebas1705.datastore.model.GameData
import es.sebas1705.models.Categories
import es.sebas1705.models.GameModel
import es.sebas1705.models.Modes

fun GameData.toModel() = GameModel(
    selectedCategories = selectedCategories
        .mapNotNull { categoryName ->
            runCatching { Categories.valueOf(categoryName) }.getOrNull()
        }
        .toSet(),
    players = players,
    mode = runCatching { Modes.valueOf(mode) }.getOrDefault(Modes.Classic),
    impostors = impostors.coerceAtLeast(1),
    showImpostorsInResult = showImpostorsInResult
)

fun GameModel.toData() = GameData(
    selectedCategories = selectedCategories.map { it.name },
    players = players,
    mode = mode.name,
    impostors = impostors,
    showImpostorsInResult = showImpostorsInResult
)

