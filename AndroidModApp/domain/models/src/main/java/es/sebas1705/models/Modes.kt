package es.sebas1705.models

/**
 * Represents the different game modes available in the application.
 *
 * @property displayName The name of the mode to be displayed in the UI.
 * @property description A brief description of the mode's gameplay mechanics.
 */
enum class Modes(
    val displayName: String,
    val description: String
) {
    Classic(
        displayName = "Classic",
        description = "The original mode with standard gameplay mechanics. Number of impostors are determined"
    ),
    Chaos(
        displayName = "Chaos",
        description = "A mode where the number of impostors is random each game, adding an element of unpredictability."
    ),
}

