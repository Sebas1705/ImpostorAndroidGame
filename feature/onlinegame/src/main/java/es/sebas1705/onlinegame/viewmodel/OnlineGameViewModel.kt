package es.sebas1705.onlinegame.viewmodel

import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.common.mvi.MVIBaseViewModel
import es.sebas1705.common.utlis.extensions.types.logD
import es.sebas1705.common.utlis.extensions.types.logI
import es.sebas1705.common.utlis.extensions.types.logW
import es.sebas1705.game.nickname.GetNicknameUseCase
import es.sebas1705.game.nickname.SaveNicknameUseCase
import es.sebas1705.game.words.SearchWordsUseCase
import es.sebas1705.models.Categories
import es.sebas1705.models.ChatMessage
import es.sebas1705.models.GameRoom
import es.sebas1705.models.Modes
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
@Suppress("TooManyFunctions", "LongMethod")
class OnlineGameViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val searchWordsUseCase: SearchWordsUseCase,
    private val firebaseFactory: FirebaseOnlineTransportFactory,
    private val getNicknameUseCase: GetNicknameUseCase,
    private val saveNicknameUseCase: SaveNicknameUseCase,
) : MVIBaseViewModel<OnlineGameUiState, OnlineGameIntent>(context) {

    private var transport: IOnlineGameTransport? = null
    private var lobby: IOnlineLobbyRepository? = null
    /** Full game state with all secrets, only held by host. */
    private var hostState: OnlineGameState = OnlineGameState()

    init {
        execute(Dispatchers.IO) {
            getNicknameUseCase().collect { nick ->
                updateUi { it.copy(savedNickname = nick) }
            }
        }
    }

    override fun initState(): OnlineGameUiState = OnlineGameUiState()

    override fun intentHandler(intent: OnlineGameIntent): Job =
        when (intent) {
            is OnlineGameIntent.Initialize -> initialize(intent)
            is OnlineGameIntent.CreateRoom -> createRoom(intent.playerName, intent.maxPlayers)
            is OnlineGameIntent.JoinRoom -> joinRoom(intent.room, intent.playerName)
            is OnlineGameIntent.UpdateGameConfig -> updateGameConfig(intent)
            OnlineGameIntent.StartGame -> startGame()
            OnlineGameIntent.MarkRevealDone -> handleMarkRevealDone()
            is OnlineGameIntent.SendChatMessage -> handleChatMessage(intent.content)
            is OnlineGameIntent.VotePlayer -> handleVote(intent.playerIndex)
            is OnlineGameIntent.SubmitGuess -> handleGuess(intent.value)
            OnlineGameIntent.AcceptResult -> handleAcceptResult()
            OnlineGameIntent.Disconnect -> disconnect()
        }

    // ── Initialize ─────────────────────────────────────────────────────────

    private fun initialize(intent: OnlineGameIntent.Initialize) = execute(Dispatchers.IO) {
        logI("initialize networkMode=${intent.networkMode}")
        updateUi {
            it.copy(
                networkMode = intent.networkMode,
                categories = intent.categories,
                mode = intent.mode,
                impostors = intent.impostors,
                isLoading = true,
            )
        }
        when (intent.networkMode) {
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
                val local = LocalNetworkTransport(
                    context,
                    OnlinePlayer(id = UUID.randomUUID().toString(), name = "")
                )
                lobby = local
                local.observeRooms().onEach { rooms ->
                    updateUi { it.copy(availableRooms = rooms, isLoading = false) }
                }.collect()
            }
        }
    }

    // ── Room management ────────────────────────────────────────────────────

    private fun createRoom(playerName: String, maxPlayers: Int) = execute(Dispatchers.IO) {
        logI("createRoom playerName=$playerName maxPlayers=$maxPlayers")
        updateUi { it.copy(isLoading = true, errorMessage = null) }

        val mode = _uiState.value.networkMode
        val player = OnlinePlayer(id = UUID.randomUUID().toString(), name = playerName, isHost = true)
        val (t, l) = buildTransport(player, mode)
        transport = t
        lobby = l

        l.createRoom(hostName = playerName, maxPlayers = maxPlayers).fold(
            onSuccess = { room ->
                if (playerName.isNotBlank()) saveNicknameUseCase(playerName)
                updateUi { s ->
                    s.copy(
                        isLoading = false,
                        isHost = true,
                        localPlayer = player,
                        currentRoom = room,
                        screen = OnlineScreen.WaitingRoom,
                        connectedPlayers = listOf(player),
                    )
                }
                // Observe connected players for both modes
                t.observeConnectedPlayers().onEach { players ->
                    if (players.isNotEmpty()) {
                        updateUi { it.copy(connectedPlayers = players) }
                    }
                }.collect()
            },
            onFailure = { err ->
                logW("createRoom failed: ${err.message}")
                updateUi { it.copy(isLoading = false, errorMessage = err.message) }
            },
        )
    }

    private fun joinRoom(room: GameRoom, playerName: String) = execute(Dispatchers.IO) {
        logI("joinRoom roomId=${room.id}")
        if (playerName.isNotBlank()) saveNicknameUseCase(playerName)
        updateUi { it.copy(isLoading = true, errorMessage = null) }

        val player = OnlinePlayer(id = UUID.randomUUID().toString(), name = playerName, isHost = false)
        when (room.networkMode) {
            NetworkMode.Local -> {
                val local = LocalNetworkTransport(context, player)
                transport = local
                lobby = local
                val joinResult = lobby?.joinRoom(room) ?: Result.failure(IllegalStateException("No lobby"))
                joinResult.onFailure { err ->
                    updateUi { it.copy(isLoading = false, errorMessage = err.message) }
                    return@execute
                }
                updateUi { it.copy(isLoading = false, isHost = false, localPlayer = player, currentRoom = room, screen = OnlineScreen.WaitingRoom) }
                // Observe player list — if empty the host disconnected
                execute(Dispatchers.IO) {
                    transport?.observeConnectedPlayers()?.collect { players ->
                        if (players.isNotEmpty()) {
                            updateUi { it.copy(connectedPlayers = players) }
                        } else if (_uiState.value.screen == OnlineScreen.WaitingRoom) {
                            transport = null
                            lobby = null
                            updateUi {
                                it.copy(
                                    screen = OnlineScreen.Lobby,
                                    currentRoom = null,
                                    connectedPlayers = emptyList(),
                                    isHost = false,
                                    errorMessage = "The host left the room.",
                                )
                            }
                        }
                    }
                }
            }
            NetworkMode.Internet -> {
                val fb = firebaseFactory.create(player).also { it.setRoomId(room.id) }
                transport = fb
                lobby = fb
                val joinResult = fb.joinRoom(room)
                joinResult.onFailure { err ->
                    updateUi { it.copy(isLoading = false, errorMessage = err.message) }
                    return@execute
                }
                updateUi { it.copy(isLoading = false, isHost = false, localPlayer = player, currentRoom = room, screen = OnlineScreen.WaitingRoom) }
                // Observe player list — if empty the host disconnected
                execute(Dispatchers.IO) {
                    transport?.observeConnectedPlayers()?.collect { players ->
                        if (players.isNotEmpty()) {
                            updateUi { it.copy(connectedPlayers = players) }
                        } else if (_uiState.value.screen == OnlineScreen.WaitingRoom) {
                            val currentRoomId = _uiState.value.currentRoom?.id
                            currentRoomId?.let { lobby?.deleteRoom(it) }
                            transport = null
                            lobby = null
                            updateUi {
                                it.copy(
                                    screen = OnlineScreen.Lobby,
                                    currentRoom = null,
                                    connectedPlayers = emptyList(),
                                    isHost = false,
                                    errorMessage = "The host left the room.",
                                )
                            }
                        }
                    }
                }
                transport?.observeGameState()?.onEach { state ->
                    val screen = when (state.step) {
                        OnlineGameStep.Lobby -> OnlineScreen.WaitingRoom
                        OnlineGameStep.Reveal -> OnlineScreen.Reveal
                        OnlineGameStep.Discussion -> OnlineScreen.Discussion
                        OnlineGameStep.Result -> OnlineScreen.Result
                    }
                    updateUi { it.copy(gameState = state, screen = screen) }
                }?.collect()
            }
        }
    }

    private fun buildTransport(
        player: OnlinePlayer,
        mode: NetworkMode,
    ): Pair<IOnlineGameTransport, IOnlineLobbyRepository> = when (mode) {
        NetworkMode.Local -> LocalNetworkTransport(context, player).let { it to it }
        NetworkMode.Internet -> firebaseFactory.create(player).let { it to it }
    }

    // ── Config update (host only, from waiting room) ───────────────────────

    private fun updateGameConfig(intent: OnlineGameIntent.UpdateGameConfig) =
        execute(Dispatchers.IO) {
            if (!_uiState.value.isHost) return@execute
            logI(
                "updateGameConfig mode=${intent.mode} impostors=${intent.impostors} " +
                    "timer=${intent.discussionTimerSeconds} impostorsKnow=${intent.impostorsKnowEachOther}"
            )
            updateUi {
                it.copy(
                    categories = intent.categories,
                    mode = intent.mode,
                    impostors = intent.impostors,
                    discussionTimerSeconds = intent.discussionTimerSeconds,
                    impostorsKnowEachOther = intent.impostorsKnowEachOther,
                    showNumOfImpostors = intent.showNumOfImpostors,
                )
            }
        }

    // ── Host: start game ───────────────────────────────────────────────────

    private fun startGame() = execute(Dispatchers.IO) {
        val state = _uiState.value
        if (!state.isHost) return@execute
        logI("startGame players=${state.connectedPlayers.size}")
        updateUi { it.copy(isLoading = true) }

        val players = state.connectedPlayers
        val words = searchWordsUseCase(state.categories.ifEmpty { Categories.entries.toSet() })
        if (words.isEmpty()) {
            logW("startGame aborted reason=no_words")
            updateUi { it.copy(isLoading = false, errorMessage = "No words available") }
            return@execute
        }

        val selectedWord = words.random()
        // Clamp impostors to valid range at the moment the game actually starts
        val impostorCount = state.impostors.coerceAtMost(players.size - 1).coerceAtLeast(1)
        val impostorIndexes = players.indices.shuffled().take(impostorCount).toSet()
        val startingIndex = players.indices.random()

        hostState = OnlineGameState(
            step = OnlineGameStep.Reveal,
            players = players,
            alivePlayerIndexes = players.indices.toSet(),
            impostorPlayerIndexes = impostorIndexes,
            word = selectedWord.word,
            clues = selectedWord.clue,
            currentSpeakerIndex = startingIndex,
            impostorCount = impostorCount,
            discussionTimerSeconds = state.discussionTimerSeconds,
            impostorsKnowEachOther = state.impostorsKnowEachOther,
            showNumOfImpostors = state.showNumOfImpostors,
        )

        broadcastPersonalizedStates()
        updateUi { it.copy(isLoading = false, screen = OnlineScreen.Reveal, gameState = buildHostViewState()) }
        collectClientActions()
    }

    private suspend fun collectClientActions() {
        transport?.observePlayerActions()?.onEach { (senderId, action) ->
            logD("clientAction sender=$senderId action=$action")
            when (action) {
                PlayerAction.MarkRevealDone -> applyRevealConfirm(senderId)
                is PlayerAction.VotePlayer -> applyVote(action.playerIndex)
                is PlayerAction.SubmitGuess -> applyGuess(action.value)
                is PlayerAction.SendChatMessage -> applyChatMessage(senderId, action.content)
                PlayerAction.AcceptResult -> applyAcceptResult(senderId)
            }
        }?.collect()
    }

    // ── Reveal phase ───────────────────────────────────────────────────────

    private fun handleMarkRevealDone() = execute(Dispatchers.IO) {
        val localId = transport?.localPlayer?.id ?: return@execute
        if (_uiState.value.isHost) {
            applyRevealConfirm(localId)
        } else {
            transport?.sendAction(PlayerAction.MarkRevealDone)
        }
    }

    private suspend fun applyRevealConfirm(playerId: String) {
        if (hostState.step != OnlineGameStep.Reveal) return
        val newConfirmed = hostState.confirmedRevealIds + playerId
        hostState = hostState.copy(confirmedRevealIds = newConfirmed)

        if (hostState.confirmedRevealIds.size >= hostState.players.size) {
            logI("revealConfirm all confirmed → Discussion")
            hostState = hostState.copy(
                step = OnlineGameStep.Discussion,
                confirmedRevealIds = emptySet(),
            )
        }
        broadcastPersonalizedStates()
        updateUi { it.copy(gameState = buildHostViewState(), screen = screenFor(hostState.step)) }
    }

    // ── Discussion phase ───────────────────────────────────────────────────

    private fun handleChatMessage(content: String) = execute(Dispatchers.IO) {
        val localId = transport?.localPlayer?.id ?: return@execute
        val localName = _uiState.value.connectedPlayers.firstOrNull { it.id == localId }?.name
            ?: transport?.localPlayer?.name ?: "?"
        if (_uiState.value.isHost) {
            applyChatMessage(localId, content)
        } else {
            transport?.sendAction(PlayerAction.SendChatMessage(content))
        }
    }

    private suspend fun applyChatMessage(senderId: String, content: String) {
        if (hostState.step != OnlineGameStep.Discussion) return
        val player = hostState.players.firstOrNull { it.id == senderId } ?: return
        val msg = ChatMessage(
            playerId = senderId,
            playerName = player.name,
            content = content,
            timestamp = System.currentTimeMillis(),
        )
        // Advance turn to next alive player.
        val alivePlayers = hostState.alivePlayerIndexes.toList().sorted()
        val currentIdx = alivePlayers.indexOf(hostState.currentSpeakerIndex)
        val nextIdx = alivePlayers.getOrElse((currentIdx + 1) % alivePlayers.size) { alivePlayers.first() }

        hostState = hostState.copy(
            chatMessages = hostState.chatMessages + msg,
            currentSpeakerIndex = nextIdx,
        )
        broadcastPersonalizedStates()
        updateUi { it.copy(gameState = buildHostViewState()) }
    }

    private fun handleVote(playerIndex: Int) = execute(Dispatchers.IO) {
        if (_uiState.value.isHost) applyVote(playerIndex)
        else transport?.sendAction(PlayerAction.VotePlayer(playerIndex))
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
        updateUi { it.copy(gameState = buildHostViewState(), screen = screenFor(hostState.step)) }
    }

    private fun handleGuess(value: String) = execute(Dispatchers.IO) {
        if (_uiState.value.isHost) applyGuess(value)
        else transport?.sendAction(PlayerAction.SubmitGuess(value))
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
        updateUi { it.copy(gameState = buildHostViewState(), screen = OnlineScreen.Result) }
    }

    // ── Result phase ───────────────────────────────────────────────────────

    private fun handleAcceptResult() = execute(Dispatchers.IO) {
        val localId = transport?.localPlayer?.id ?: return@execute
        if (_uiState.value.isHost) applyAcceptResult(localId)
        else transport?.sendAction(PlayerAction.AcceptResult)
    }

    private suspend fun applyAcceptResult(playerId: String) {
        if (hostState.step != OnlineGameStep.Result) return
        val newAccepted = hostState.acceptedResultIds + playerId
        hostState = hostState.copy(acceptedResultIds = newAccepted)

        if (newAccepted.size >= hostState.players.size) {
            logI("allAccepted → back to WaitingRoom")
            hostState = OnlineGameState(
                step = OnlineGameStep.Lobby,
                players = hostState.players,
            )
            broadcastPersonalizedStates()
            updateUi { it.copy(gameState = buildHostViewState(), screen = OnlineScreen.WaitingRoom) }
        } else {
            broadcastPersonalizedStates()
            updateUi { it.copy(gameState = buildHostViewState()) }
        }
    }

    // ── Personalized broadcast ─────────────────────────────────────────────

    private suspend fun broadcastPersonalizedStates() {
        val t = transport ?: return
        hostState.players.forEachIndexed { index, player ->
            val isImpostor = index in hostState.impostorPlayerIndexes
            val showImpostors = hostState.step == OnlineGameStep.Result
            val personalized = hostState.copy(
                isImpostor = isImpostor,
                word = if (isImpostor) null else hostState.word,
                clues = if (isImpostor) hostState.clues else emptyList(),
                impostorPlayerIndexes = if (showImpostors) hostState.impostorPlayerIndexes else emptySet(),
            )
            t.sendStateTo(player.id, personalized)
        }
    }

    private fun buildHostViewState(): OnlineGameState {
        val localId = transport?.localPlayer?.id ?: return hostState
        val hostIndex = hostState.players.indexOfFirst { it.id == localId }
        val isImpostor = hostIndex in hostState.impostorPlayerIndexes
        val showImpostors = hostState.step == OnlineGameStep.Result
        return hostState.copy(
            isImpostor = isImpostor,
            word = if (isImpostor) null else hostState.word,
            clues = if (isImpostor) hostState.clues else emptyList(),
            impostorPlayerIndexes = if (showImpostors) hostState.impostorPlayerIndexes else emptySet(),
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

    private fun screenFor(step: OnlineGameStep) = when (step) {
        OnlineGameStep.Lobby -> OnlineScreen.WaitingRoom
        OnlineGameStep.Reveal -> OnlineScreen.Reveal
        OnlineGameStep.Discussion -> OnlineScreen.Discussion
        OnlineGameStep.Result -> OnlineScreen.Result
    }

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
            winner = winner, reason = reason,
            word = state.word.orEmpty(),
            impostorNames = state.impostorPlayerIndexes.map { state.players[it].name },
            correctVotes = correct, incorrectVotes = incorrect,
        )
    }

    private fun normalize(text: String): String {
        val n = Normalizer.normalize(text.trim(), Normalizer.Form.NFD)
        return n.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "").lowercase()
    }
}
