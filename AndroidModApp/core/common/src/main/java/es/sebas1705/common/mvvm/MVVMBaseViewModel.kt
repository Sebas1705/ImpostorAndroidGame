package es.sebas1705.common.mvvm

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.sebas1705.common.utlis.extensions.composables.printTextInToast
import es.sebas1705.common.utlis.extensions.types.logW
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

/**
 * Base class for the MVVMViewModel
 *
 * @property context [Context]: application context
 * @property loading [MutableStateFlow]<[Boolean]>: loading state
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@SuppressLint("StaticFieldLeak")
abstract class MVVMBaseViewModel(
    protected val context: Context
) : ViewModel() {

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
        _loading.value = true
    }

    /**
     * Stop the loading state
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    protected fun stopLoading() {
        _loading.value = false
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

