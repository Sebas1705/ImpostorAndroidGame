package es.sebas1705.datastore.model

data class GameData(
    val selectedCategories: List<String> = emptyList(),
    val players: List<String> = emptyList(),
    val mode: String = "Classic",
    val impostors: Int = 1,
    val showImpostorsInResult: Boolean = true
)

