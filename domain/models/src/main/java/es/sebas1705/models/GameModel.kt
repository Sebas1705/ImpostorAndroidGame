package es.sebas1705.models

data class GameModel(
    val selectedCategories: Set<Categories> = emptySet(),
    val players: List<String> = emptyList(),
    val mode: Modes = Modes.Classic,
    val impostors: Int = 1,
    val showImpostorsInResult: Boolean = true,
    val discussionTimerSeconds: Int = 180,
    val impostorsKnowEachOther: Boolean = false,
    val showNumOfImpostors: Boolean = false,
)

