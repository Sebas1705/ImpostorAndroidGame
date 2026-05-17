package es.sebas1705.models

import kotlinx.serialization.Serializable

/**
 * Serializable game state sent from host to each client.
 * The host generates a personalized copy per client so impostors
 * never receive the civilians' word and vice-versa.
 */
@Serializable
data class OnlineGameState(
    val step: OnlineGameStep = OnlineGameStep.Lobby,
    val players: List<OnlinePlayer> = emptyList(),
    val alivePlayerIndexes: Set<Int> = emptySet(),
    val impostorPlayerIndexes: Set<Int> = emptySet(),
    val currentRevealIndex: Int = 0,
    val revealedCurrentCard: Boolean = false,
    val currentSpeakerIndex: Int = 0,
    val word: String? = null,
    val clues: List<String> = emptyList(),
    val isImpostor: Boolean = false,
    val correctVotes: Int = 0,
    val incorrectVotes: Int = 0,
    val guessFeedback: String? = null,
    val result: OnlineGameResult? = null,
    val discussionTimerSeconds: Int = 180,
    val impostorsKnowEachOther: Boolean = false,
    val showNumOfImpostors: Boolean = false,
    val impostorCount: Int = 0,
) {
    val currentPlayerName: String
        get() = players.getOrNull(currentRevealIndex)?.name ?: "Unknown"

    val hasMoreRevealPlayers: Boolean
        get() = currentRevealIndex < players.lastIndex
}
