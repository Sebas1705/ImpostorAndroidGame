package es.sebas1705.home.nav.offline.viewmodel

import es.sebas1705.common.mvi.MVIBaseState
import es.sebas1705.models.GameWordEntry

enum class OfflineGameStep {
    Reveal,
    Discussion,
    Result
}

enum class OfflineWinner {
    Civilians,
    Impostors,
    Tie
}

data class OfflineGameResult(
    val winner: OfflineWinner,
    val reason: String,
    val word: String,
    val impostorNames: List<String>,
    val correctVotes: Int,
    val incorrectVotes: Int
)

data class OfflineGameState(
    val isDebugBuild: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val step: OfflineGameStep = OfflineGameStep.Reveal,
    val players: List<String> = emptyList(),
    val alivePlayerIndexes: Set<Int> = emptySet(),
    val impostorPlayerIndexes: Set<Int> = emptySet(),
    val currentRevealIndex: Int = 0,
    val revealedCurrentCard: Boolean = false,
    val currentSpeakerIndex: Int = 0,
    val wordEntry: GameWordEntry? = null,
    val correctVotes: Int = 0,
    val incorrectVotes: Int = 0,
    val guessFeedback: String? = null,
    val result: OfflineGameResult? = null
) : MVIBaseState {
    val hasMoreRevealPlayers: Boolean
        get() = currentRevealIndex < players.lastIndex

    val currentPlayerName: String
        get() = players.getOrElse(currentRevealIndex) { "Unknown" }
}

