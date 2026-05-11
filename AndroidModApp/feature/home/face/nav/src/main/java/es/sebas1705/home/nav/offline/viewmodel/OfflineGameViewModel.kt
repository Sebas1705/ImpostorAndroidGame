package es.sebas1705.home.nav.offline.viewmodel

import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.common.mvi.MVIBaseViewModel
import es.sebas1705.core.resources.R
import es.sebas1705.game.RecordOfflineMatchResultUseCase
import es.sebas1705.game.SearchGameWordsUseCase
import es.sebas1705.models.Modes
import kotlinx.coroutines.Dispatchers
import java.text.Normalizer
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
@Suppress("TooManyFunctions")
class OfflineGameViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val searchGameWordsUseCase: SearchGameWordsUseCase,
    private val recordOfflineMatchResultUseCase: RecordOfflineMatchResultUseCase
) : MVIBaseViewModel<OfflineGameState, OfflineGameIntent>(context) {

    override fun initState(): OfflineGameState = OfflineGameState()

    override fun intentHandler(intent: OfflineGameIntent) {
        when (intent) {
            is OfflineGameIntent.Initialize -> initialize(intent)
            OfflineGameIntent.MarkRevealDone -> markRevealDone()
            OfflineGameIntent.NextRevealPlayer -> nextRevealPlayer()
            is OfflineGameIntent.VotePlayer -> votePlayer(intent.playerIndex)
            is OfflineGameIntent.SubmitGuess -> submitGuess(intent.value)
        }
    }

    private fun initialize(intent: OfflineGameIntent.Initialize) = execute(Dispatchers.IO) {
        val players = intent.players
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .distinct()

        if (players.size < 3) {
            updateUi {
                it.copy(errorMessage = context.getString(R.string.core_resources_game_error_min_players))
            }
            return@execute
        }

        if (intent.categories.isEmpty()) {
            updateUi {
                it.copy(errorMessage = context.getString(R.string.core_resources_game_error_select_category))
            }
            return@execute
        }

        updateUi { it.copy(isLoading = true, errorMessage = null) }

        val words = searchGameWordsUseCase(intent.categories)
        if (words.isEmpty()) {
            updateUi {
                it.copy(
                    isLoading = false,
                    errorMessage = context.getString(R.string.core_resources_game_error_no_words)
                )
            }
            return@execute
        }

        val selectedWord = words.random()
        val impostorCount = resolveImpostorCount(
            mode = intent.mode,
            requested = intent.impostors,
            playerCount = players.size
        )

        val impostorIndexes = players.indices.shuffled().take(impostorCount).toSet()
        val startingPlayerIndex = players.indices.random()

        updateUi {
            it.copy(
                isDebugBuild = intent.isDebugBuild,
                isLoading = false,
                errorMessage = null,
                step = OfflineGameStep.Reveal,
                players = players,
                alivePlayerIndexes = players.indices.toSet(),
                impostorPlayerIndexes = impostorIndexes,
                currentRevealIndex = 0,
                revealedCurrentCard = false,
                currentSpeakerIndex = startingPlayerIndex,
                wordEntry = selectedWord,
                correctVotes = 0,
                incorrectVotes = 0,
                guessFeedback = null,
                result = null
            )
        }
    }

    private fun markRevealDone() {
        updateUi { state ->
            if (state.step != OfflineGameStep.Reveal) {
                state
            } else {
                state.copy(revealedCurrentCard = true)
            }
        }
    }

    private fun nextRevealPlayer() {
        updateUi { state ->
            if (state.step != OfflineGameStep.Reveal || !state.revealedCurrentCard) {
                return@updateUi state
            }

            if (!state.hasMoreRevealPlayers) {
                return@updateUi state.copy(
                    step = OfflineGameStep.Discussion,
                    guessFeedback = context.getString(
                        R.string.core_resources_game_starts_discussion,
                        state.players[state.currentSpeakerIndex]
                    )
                )
            }

            state.copy(
                currentRevealIndex = state.currentRevealIndex + 1,
                revealedCurrentCard = false
            )
        }
    }

    private fun votePlayer(playerIndex: Int) {
        updateUi { state ->
            if (state.step != OfflineGameStep.Discussion || playerIndex !in state.alivePlayerIndexes) {
                return@updateUi state
            }

            val isImpostor = playerIndex in state.impostorPlayerIndexes
            val newAlive = state.alivePlayerIndexes - playerIndex
            val correctVotes = if (isImpostor) state.correctVotes + 1 else state.correctVotes
            val incorrectVotes = if (isImpostor) state.incorrectVotes else state.incorrectVotes + 1
            val result = buildResultIfFinished(state, newAlive, correctVotes, incorrectVotes)

            if (result == null) {
                val voteFeedback = if (state.isDebugBuild) {
                    if (isImpostor) {
                        context.getString(
                            R.string.core_resources_game_vote_reveal_impostor,
                            state.players[playerIndex]
                        )
                    } else {
                        context.getString(
                            R.string.core_resources_game_vote_reveal_civilian,
                            state.players[playerIndex]
                        )
                    }
                } else {
                    context.getString(
                        R.string.core_resources_game_vote_reveal_neutral,
                        state.players[playerIndex]
                    )
                }

                state.copy(
                    alivePlayerIndexes = newAlive,
                    correctVotes = correctVotes,
                    incorrectVotes = incorrectVotes,
                    guessFeedback = voteFeedback
                )
            } else {
                recordRankingForWinner(
                    players = state.players,
                    impostorIndexes = state.impostorPlayerIndexes,
                    winner = result.winner
                )
                state.copy(
                    alivePlayerIndexes = newAlive,
                    correctVotes = correctVotes,
                    incorrectVotes = incorrectVotes,
                    step = OfflineGameStep.Result,
                    result = result,
                    guessFeedback = null
                )
            }
        }
    }

    private fun submitGuess(value: String) {
        updateUi { state ->
            if (state.step != OfflineGameStep.Discussion || state.wordEntry == null) {
                return@updateUi state
            }

            val guess = normalize(value)
            val solution = normalize(state.wordEntry.word)
            if (guess.isBlank()) {
                return@updateUi state.copy(
                    guessFeedback = context.getString(R.string.core_resources_game_guess_empty)
                )
            }

            if (guess == solution) {
                recordRankingForWinner(
                    players = state.players,
                    impostorIndexes = state.impostorPlayerIndexes,
                    winner = OfflineWinner.Impostors
                )
                state.copy(
                    step = OfflineGameStep.Result,
                    result = OfflineGameResult(
                        winner = OfflineWinner.Impostors,
                        reason = context.getString(R.string.core_resources_game_reason_guess_win),
                        word = state.wordEntry.word,
                        impostorNames = state.impostorPlayerIndexes.map { state.players[it] },
                        correctVotes = state.correctVotes,
                        incorrectVotes = state.incorrectVotes
                    ),
                    guessFeedback = null
                )
            } else {
                state.copy(guessFeedback = context.getString(R.string.core_resources_game_guess_wrong))
            }
        }
    }

    private fun buildResultIfFinished(
        state: OfflineGameState,
        aliveIndexes: Set<Int>,
        correctVotes: Int,
        incorrectVotes: Int
    ): OfflineGameResult? {
        val aliveImpostors = aliveIndexes.intersect(state.impostorPlayerIndexes)
        val aliveCivilians = aliveIndexes - state.impostorPlayerIndexes

        val winner = when {
            aliveImpostors.isEmpty() -> OfflineWinner.Civilians
            aliveCivilians.isEmpty() -> OfflineWinner.Impostors
            aliveCivilians.size == 1 && aliveImpostors.size == 1 -> OfflineWinner.Tie
            else -> return null
        }

        val reason = when (winner) {
            OfflineWinner.Civilians -> context.getString(
                R.string.core_resources_game_reason_all_impostors_voted
            )
            OfflineWinner.Impostors -> context.getString(R.string.core_resources_game_reason_no_civilians)
            OfflineWinner.Tie -> context.getString(R.string.core_resources_game_reason_tie)
        }

        return OfflineGameResult(
            winner = winner,
            reason = reason,
            word = state.wordEntry?.word.orEmpty(),
            impostorNames = state.impostorPlayerIndexes.map { state.players[it] },
            correctVotes = correctVotes,
            incorrectVotes = incorrectVotes
        )
    }

    private fun resolveImpostorCount(mode: Modes, requested: Int, playerCount: Int): Int {
        val maxImpostors = (playerCount - 1).coerceAtLeast(1)
        return when (mode) {
            Modes.Classic -> requested.coerceIn(1, maxImpostors)
            Modes.Chaos -> Random.nextInt(from = 1, until = maxImpostors + 1)
        }
    }

    private fun normalize(text: String): String {
        val normalized = Normalizer.normalize(text.trim(), Normalizer.Form.NFD)
        return normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "").lowercase()
    }

    private fun recordRankingForWinner(
        players: List<String>,
        impostorIndexes: Set<Int>,
        winner: OfflineWinner
    ) {
        val impostorNames = impostorIndexes.map { players[it] }.toSet()
        val civilianNames = players.filterIndexed { index, _ -> index !in impostorIndexes }.toSet()

        val civilianWinnerNames = when (winner) {
            OfflineWinner.Civilians -> civilianNames
            OfflineWinner.Impostors,
            OfflineWinner.Tie -> emptySet()
        }
        val impostorWinnerNames = when (winner) {
            OfflineWinner.Impostors -> impostorNames
            OfflineWinner.Civilians,
            OfflineWinner.Tie -> emptySet()
        }

        if (civilianWinnerNames.isEmpty() && impostorWinnerNames.isEmpty()) return

        execute(Dispatchers.IO) {
            recordOfflineMatchResultUseCase(
                civilianWinnerNames = civilianWinnerNames,
                impostorWinnerNames = impostorWinnerNames
            )
        }
    }
}


