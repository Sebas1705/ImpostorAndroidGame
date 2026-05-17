package es.sebas1705.onlinegame.viewmodel

import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.common.mvi.MVIBaseViewModel
import es.sebas1705.common.utlis.extensions.types.logD
import es.sebas1705.common.utlis.extensions.types.logI
import es.sebas1705.common.utlis.extensions.types.logW
import es.sebas1705.game.words.SearchWordsUseCase
import es.sebas1705.models.Categories
import es.sebas1705.models.GameRoom
import es.sebas1705.models.NetworkMode
import es.sebas1705.models.OnlineGameResult
import es.sebas1705.models.OnlineGameState
import es.sebas1705.models.OnlineGameStep
import es.sebas1705.models.OnlinePlayer
import es.sebas1705.models.OnlineWinner
import es.sebas1705.models.PlayerAction
import es.sebas1705.network.interfaces.IOnlineGameTransport
import es.sebas1705.network.interfaces.IOnlineLobbyRepository
import es.sebas1705.network.transport.LocalNetworkTransport
import es.sebas1705.repositories.online.FirebaseOnlineTransportFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.text.Normalizer
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
@Suppress("TooManyFunctions")
class OnlineGameViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val searchWordsUseCase: SearchWordsUseCase,
    private val firebaseFactory: FirebaseOnlineTransportFactory,
) : MVIBaseViewModel<OnlineGameUiState, OnlineGameIntent>(context) {

    private var transport: IOnlineGameTransport? = null
    private var lobby: IOnlineLobbyRepository? = null
    private var hostState: OnlineGameState = OnlineGameState()

    override fun initState(): OnlineGameUiState = OnlineGameUiState()

    override fun intentHandler(intent: OnlineGameIntent): Job =
        when (intent) {
            is OnlineGameIntent.SelectMode -> selectMode(intent.mode)
            is OnlineGameIntent.CreateRoom -> createRoom(intent.playerName, intent.maxPlayers)
            is OnlineGameIntent.JoinRoom -> joinRoom(intent.room, intent.playerName)
            OnlineGameIntent.StartGame -> startGame()
            OnlineGameIntent.MarkRevealDone -> handleMarkRevealDone()
            OnlineGameIntent.NextRevealPlayer -> handleNextRevealPlayer()
            is OnlineGameIntent.VotePlayer -> handleVote(intent.playerIndex)
            is OnlineGameIntent.SubmitGuess -> handleGuess(intent.value)
            OnlineGameIntent.Disconnect -> disconnect()
        }

    // ── Mode selection ─────────────────────────────────────────────────────

    private fun selectMode(mode: NetworkMode) = execute(Dispatchers.IO) {
        logI("selectMode mode=$mode")
        updateUi { it.copy(selectedMode = mode, screen = OnlineScreen.Lobby, isLoading = true) }
        when (mode) {
            NetworkMode.Internet -> {
                val fb = firebaseFactory.create(
                    OnlinePlayer(id = UUID.randomUUID().toString(), name = "", isHost = false)
                )
                lobby = fb
                fb.observeRooms().onEach { rooms ->
                    updateUi { it.copy(availableRooms = rooms, isLoading = false) }
                }.collect()
            }
            NetworkMode.Local -> {
                val dummyPlayer = OnlinePlayer(id = UUID.randomUUID().toString(), name = "")
                val localLobby = LocalNetworkTransport(context, dummyPlayer)
                lobby = localLobby
                localLobby.observeRooms().onEach { rooms ->
                    updateUi { it.copy(availableRooms = rooms, isLoading = false) }
                }.collect()
            }
        }
    }

    // ── Room management ────────────────────────────────────────────────────

    private fun createRoom(playerName: String, maxPlayers: Int) = execute(Dispatchers.IO) {
        logI("createRoom playerName=$playerName maxPlayers=$maxPlayers")
        updateUi { it.copy(isLoading = true, errorMessage = null) }

        val mode = _uiState.value.selectedMode ?: NetworkMode.Local
        val player = OnlinePlayer(id = UUID.randomUUID().toString(), name = playerName, isHost = true)
        val (t, l) = buildTransport(player, mode)
        transport = t
        lobby = l

        l.createRoom(hostName = playerName, maxPlayers = maxPlayers).fold(
            onSuccess = { room ->
                updateUi { state ->
                    state.copy(
                        isLoading = false,
                        isHost = true,
                        currentRoom = room,
                        screen = OnlineScreen.WaitingRoom,
                        connectedPlayers = listOf(player),
                    )
                }
                if (mode == NetworkMode.Local) {
                    t.observeConnectedPlayers().onEach { players ->
                        updateUi { it.copy(connectedPlayers = players) }
                    }.collect()
                }
                logI("createRoom success roomId=${room.id}")
            },
            onFailure = { err ->
                logW("createRoom failed: ${err.message}")
                updateUi { it.copy(isLoading = false, errorMessage = err.message) }
            },
        )
    }

    private fun joinRoom(room: GameRoom, playerName: String) = execute(Dispatchers.IO) {
        logI("joinRoom roomId=${room.id} host=${room.hostName}")
        updateUi { it.copy(isLoading = true, errorMessage = null) }

        val player = OnlinePlayer(id = UUID.randomUUID().toString(), name = playerName, isHost = false)
        when (room.networkMode) {
            NetworkMode.Local -> {
                val local = LocalNetworkTransport(context, player)
                transport = local
                lobby = local
                local.joinRoom(room)
            }
            NetworkMode.Internet -> {
                val fb = firebaseFactory.create(player).also { it.setRoomId(room.id) }
                transport = fb
                lobby = fb
            }
        }

        updateUi { it.copy(isLoading = false, isHost = false, currentRoom = room, screen = OnlineScreen.WaitingRoom) }

        // Collect game state from host until the game ends.
        transport?.observeGameState()?.onEach { state ->
            updateUi { it.copy(gameState = state, screen = OnlineScreen.Game) }
        }?.collect()
    }

    private fun buildTransport(player: OnlinePlayer, mode: NetworkMode): Pair<IOnlineGameTransport, IOnlineLobbyRepository> =
        when (mode) {
            NetworkMode.Local -> LocalNetworkTransport(context, player).let { it to it }
            NetworkMode.Internet -> firebaseFactory.create(player).let { it to it }
        }

    // ── Host: start game ───────────────────────────────────────────────────

    private fun startGame() = execute(Dispatchers.IO) {
        val state = _uiState.value
        if (!state.isHost) return@execute

        logI("startGame players=${state.connectedPlayers.size}")
        updateUi { it.copy(isLoading = true) }

        val players = state.connectedPlayers
        val words = searchWordsUseCase(Categories.entries.toSet())
        if (words.isEmpty()) {
            logW("startGame aborted reason=no_words")
            updateUi { it.copy(isLoading = false, errorMessage = "No words available") }
            return@execute
        }

        val selectedWord = words.random()
        val impostorCount = 1.coerceAtMost(players.size - 1)
        val impostorIndexes = players.indices.shuffled().take(impostorCount).toSet()
        val startingIndex = players.indices.random()

        hostState = OnlineGameState(
            step = OnlineGameStep.Reveal,
            players = players,
            alivePlayerIndexes = players.indices.toSet(),
            impostorPlayerIndexes = impostorIndexes,
            currentRevealIndex = 0,
            revealedCurrentCard = false,
            currentSpeakerIndex = startingIndex,
            word = selectedWord.word,
            clues = selectedWord.clue,
            discussionTimerSeconds = 180,
        )

        broadcastPersonalizedStates()
        updateUi { it.copy(isLoading = false, screen = OnlineScreen.Game, gameState = buildHostViewState()) }

        // Collect client actions.
        transport?.observePlayerActions()?.onEach { (_, action) ->
            logD("clientAction action=$action")
            when (action) {
                is PlayerAction.VotePlayer -> applyVote(action.playerIndex)
                is PlayerAction.SubmitGuess -> applyGuess(action.value)
                PlayerAction.MarkRevealDone -> applyRevealDone()
            }
        }?.collect()
    }

    // ── Host: in-game logic ────────────────────────────────────────────────

    private fun handleMarkRevealDone() = execute(Dispatchers.IO) {
        if (_uiState.value.isHost) applyRevealDone()
        else transport?.sendAction(PlayerAction.MarkRevealDone)
    }

    private fun handleNextRevealPlayer() = execute(Dispatchers.IO) {
        if (_uiState.value.isHost) applyNextRevealPlayer()
    }

    private fun handleVote(playerIndex: Int) = execute(Dispatchers.IO) {
        if (_uiState.value.isHost) applyVote(playerIndex)
        else transport?.sendAction(PlayerAction.VotePlayer(playerIndex))
    }

    private fun handleGuess(value: String) = execute(Dispatchers.IO) {
        if (_uiState.value.isHost) applyGuess(value)
        else transport?.sendAction(PlayerAction.SubmitGuess(value))
    }

    private suspend fun applyRevealDone() {
        if (hostState.step != OnlineGameStep.Reveal) return
        hostState = hostState.copy(revealedCurrentCard = true)
        applyNextRevealPlayer()
    }

    private suspend fun applyNextRevealPlayer() {
        if (hostState.step != OnlineGameStep.Reveal) return
        hostState = if (!hostState.hasMoreRevealPlayers) {
            hostState.copy(step = OnlineGameStep.Discussion)
        } else {
            hostState.copy(currentRevealIndex = hostState.currentRevealIndex + 1, revealedCurrentCard = false)
        }
        broadcastPersonalizedStates()
        updateUi { it.copy(gameState = buildHostViewState()) }
    }

    private suspend fun applyVote(playerIndex: Int) {
        val s = hostState
        if (s.step != OnlineGameStep.Discussion || playerIndex !in s.alivePlayerIndexes) return

        val isImpostor = playerIndex in s.impostorPlayerIndexes
        val newAlive = s.alivePlayerIndexes - playerIndex
        val correct = if (isImpostor) s.correctVotes + 1 else s.correctVotes
        val incorrect = if (!isImpostor) s.incorrectVotes + 1 else s.incorrectVotes
        val result = buildResult(s, newAlive, correct, incorrect)

        hostState = s.copy(
            alivePlayerIndexes = newAlive,
            correctVotes = correct,
            incorrectVotes = incorrect,
            step = if (result != null) OnlineGameStep.Result else s.step,
            result = result,
        )
        broadcastPersonalizedStates()
        updateUi { it.copy(gameState = buildHostViewState()) }
    }

    private suspend fun applyGuess(value: String) {
        val s = hostState
        if (s.step != OnlineGameStep.Discussion) return
        val word = s.word ?: return

        if (normalize(value) != normalize(word)) {
            hostState = s.copy(guessFeedback = "Wrong guess")
            broadcastPersonalizedStates()
            return
        }

        val result = OnlineGameResult(
            winner = OnlineWinner.Impostors,
            reason = "Impostors guessed the word",
            word = word,
            impostorNames = s.impostorPlayerIndexes.map { s.players[it].name },
            correctVotes = s.correctVotes,
            incorrectVotes = s.incorrectVotes,
        )
        hostState = s.copy(step = OnlineGameStep.Result, result = result, guessFeedback = null)
        broadcastPersonalizedStates()
        updateUi { it.copy(gameState = buildHostViewState()) }
    }

    // ── Personalized state broadcast ───────────────────────────────────────

    private suspend fun broadcastPersonalizedStates() {
        val t = transport ?: return
        hostState.players.forEachIndexed { index, player ->
            val isImpostor = index in hostState.impostorPlayerIndexes
            val personalized = hostState.copy(
                isImpostor = isImpostor,
                word = if (isImpostor) null else hostState.word,
                clues = if (isImpostor) emptyList() else hostState.clues,
                impostorPlayerIndexes = if (hostState.step == OnlineGameStep.Result)
                    hostState.impostorPlayerIndexes else emptySet(),
            )
            t.sendStateTo(player.id, personalized)
        }
    }

    private fun buildHostViewState(): OnlineGameState {
        val localId = transport?.localPlayer?.id ?: return hostState
        val hostIndex = hostState.players.indexOfFirst { it.id == localId }
        val isImpostor = hostIndex in hostState.impostorPlayerIndexes
        return hostState.copy(
            isImpostor = isImpostor,
            word = if (isImpostor) null else hostState.word,
            clues = if (isImpostor) emptyList() else hostState.clues,
            impostorPlayerIndexes = if (hostState.step == OnlineGameStep.Result)
                hostState.impostorPlayerIndexes else emptySet(),
        )
    }

    // ── Disconnect ─────────────────────────────────────────────────────────

    private fun disconnect() = execute(Dispatchers.IO) {
        logI("disconnect")
        transport?.disconnect()
        transport = null
        lobby = null
        hostState = OnlineGameState()
        updateUi { OnlineGameUiState() }
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    private fun buildResult(
        state: OnlineGameState,
        alive: Set<Int>,
        correct: Int,
        incorrect: Int,
    ): OnlineGameResult? {
        val aliveImpostors = alive.intersect(state.impostorPlayerIndexes)
        val aliveCivilians = alive - state.impostorPlayerIndexes
        val (winner, reason) = when {
            aliveImpostors.isEmpty() -> OnlineWinner.Civilians to "All impostors eliminated"
            aliveCivilians.isEmpty() -> OnlineWinner.Impostors to "No civilians left"
            aliveImpostors.size > aliveCivilians.size -> OnlineWinner.Impostors to "Impostors outnumber civilians"
            aliveCivilians.size == 1 && aliveImpostors.size == 1 -> OnlineWinner.Tie to "Final duel"
            else -> return null
        }
        return OnlineGameResult(
            winner = winner,
            reason = reason,
            word = state.word.orEmpty(),
            impostorNames = state.impostorPlayerIndexes.map { state.players[it].name },
            correctVotes = correct,
            incorrectVotes = incorrect,
        )
    }

    private fun normalize(text: String): String {
        val n = Normalizer.normalize(text.trim(), Normalizer.Form.NFD)
        return n.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "").lowercase()
    }
}
