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
    // ── Reveal phase ──────────────────────────────────────────────────────
    val word: String? = null,
    val clues: List<String> = emptyList(),
    val isImpostor: Boolean = false,
    /** IDs of players who have confirmed they saw their card. */
    val confirmedRevealIds: Set<String> = emptySet(),
    // ── Discussion phase ──────────────────────────────────────────────────
    val currentSpeakerIndex: Int = 0,
    val chatMessages: List<ChatMessage> = emptyList(),
    val correctVotes: Int = 0,
    val incorrectVotes: Int = 0,
    val guessFeedback: String? = null,
    // ── Result phase ──────────────────────────────────────────────────────
    val result: OnlineGameResult? = null,
    /** IDs of players who accepted the result (to return to room). */
    val acceptedResultIds: Set<String> = emptySet(),
    // ── Config (broadcast to clients) ─────────────────────────────────────
    val discussionTimerSeconds: Int = 180,
    val impostorsKnowEachOther: Boolean = false,
    val showNumOfImpostors: Boolean = false,
    val impostorCount: Int = 0,
) {
    val currentPlayerName: String
        get() = players.getOrNull(currentSpeakerIndex)?.name ?: "Unknown"

    val allPlayersConfirmedReveal: Boolean
        get() = players.isNotEmpty() && confirmedRevealIds.size >= players.size

    val allPlayersAcceptedResult: Boolean
        get() = players.isNotEmpty() && acceptedResultIds.size >= players.size
}
