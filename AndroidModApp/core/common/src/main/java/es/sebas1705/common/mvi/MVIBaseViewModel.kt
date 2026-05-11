package es.sebas1705.common.mvi

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.sebas1705.common.utlis.extensions.composables.printTextInToast
import es.sebas1705.common.utlis.extensions.types.logW
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

/**
 * Base class for the MVIBaseViewModel
 *
 * @param S [MVIBaseState]: State of the viewModel
 * @param I [MVIBaseIntent]: Intent of the viewModel
 *
 * @property context [Context]: Context of the application
 * @property initialState [S]: Initial state of the viewModel
 * @property uiState [MutableStateFlow]<[S]>: UiState for the composable
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@SuppressLint("StaticFieldLeak")
abstract class MVIBaseViewModel<S : MVIBaseState, I : MVIBaseIntent>(
    protected val context: Context
) : ViewModel() {

    private val initialState: S by lazy { initState() }
    protected val _uiState: MutableStateFlow<S> by lazy { MutableStateFlow(initialState) }
    val uiState
        get() = _uiState
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                initialState
            )

    private val _loading = MutableStateFlow(false)
    val loading
        get() = _loading.asStateFlow()

    companion object {
        private val instances_num = AtomicInteger(0)
    }

    init {
        logW("ViewModel initialized (new number of instances: ${instances_num.addAndGet(1)})")
    }

    override fun onCleared() {
        super.onCleared()
        logW("ViewModel cleared (new number of instances: ${instances_num.addAndGet(-1)})")
    }

    /**
     * Initial state of the viewModel
     *
     * @return [S]
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    protected abstract fun initState(): S

    /**
     * Handle the intents from the composable
     *
     * @param intent [I]: Intent from the composable
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    protected abstract fun intentHandler(intent: I)

    /**
     * Receive the intents from the composable
     *
     * @param intent [I]: Intent from the composable
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun eventHandler(intent: I) {
        intentHandler(intent)
    }

    /**
     * Update the uiState
     *
     * @param handler [suspend] (intent: S) -> S: handler to update the uiState
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    protected fun updateUi(
        handler: suspend (intent: S) -> S,
    ) = execute {
        _uiState.update { handler(it) }
    }

    /**
     * Execute the actions in the viewModelScope
     *
     * @param dispatcher [CoroutineDispatcher]: dispatcher to execute the action
     * @param action [suspend] (): Unit: action to be executed
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    protected fun execute(
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        action: suspend () -> Unit
    ) = viewModelScope.launch(dispatcher) { action() }

    /**
     * Start the loading state
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    protected fun startLoading() {
        _loading.update { true }
    }

    /**
     * Stop the loading state
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    protected fun stopLoading() {
        _loading.update { false }
    }

    /**
     * Stop the loading state and show an error
     *
     * @param error [String]: error to show
     * @param onError [(String) -> Unit]: action to be executed when the error is shown
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    protected fun stopAndError(
        error: String,
        onError: (String) -> Unit = context::printTextInToast
    ) {
        stopLoading()
        execute {
            onError(error)
        }
    }

}

