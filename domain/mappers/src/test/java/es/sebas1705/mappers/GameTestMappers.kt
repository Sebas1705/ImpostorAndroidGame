package es.sebas1705.mappers

import es.sebas1705.datastore.model.GameData
import es.sebas1705.models.Categories
import es.sebas1705.models.GameModel
import es.sebas1705.models.Modes
import org.junit.Assert.assertEquals
import org.junit.Test

class GameTestMappers {

    @Test
    fun `GameData to GameModel conversion`() {
        val gameData = GameData(
            selectedCategories = listOf(Categories.ANIMALS.name),
            players = listOf("Ana", "Luis"),
            mode = Modes.Chaos.name,
            impostors = 2,
            showImpostorsInResult = false
        )

        val gameModel = gameData.toModel()

        assertEquals(setOf(Categories.ANIMALS), gameModel.selectedCategories)
        assertEquals(listOf("Ana", "Luis"), gameModel.players)
        assertEquals(Modes.Chaos, gameModel.mode)
        assertEquals(2, gameModel.impostors)
        assertEquals(false, gameModel.showImpostorsInResult)
    }

    @Test
    fun `GameModel to GameData conversion`() {
        val gameModel = GameModel(
            selectedCategories = setOf(Categories.SCIENCE_CHEMISTRY),
            players = listOf("Nora", "Pablo"),
            mode = Modes.Chaos,
            impostors = 3,
            showImpostorsInResult = false
        )

        val gameData = gameModel.toData()

        assertEquals(listOf(Categories.SCIENCE_CHEMISTRY.name), gameData.selectedCategories)
        assertEquals(listOf("Nora", "Pablo"), gameData.players)
        assertEquals(Modes.Chaos.name, gameData.mode)
        assertEquals(3, gameData.impostors)
        assertEquals(false, gameData.showImpostorsInResult)
    }
}

