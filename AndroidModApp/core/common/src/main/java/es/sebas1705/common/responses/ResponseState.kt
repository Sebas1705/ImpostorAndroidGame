package es.sebas1705.common.responses

import es.sebas1705.common.managers.ClassLogData

/**
 * Sealed class to represent the state of the data
 *
 * @param T: Type of the response
 *
 * @property Loading [ResponseState]: Loading state
 * @property Success [ResponseState]: Success state
 * @property EmptySuccess [ResponseState]: Empty success state
 * @property Error [ResponseState]: Error state
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
sealed class ResponseState<out T> {

    /**
     * Catcher function to handle the different states of the response
     *
     * @param onLoading () -> Unit: Function to handle the loading state
     * @param onSuccess (T) -> Unit: Function to handle the success state
     * @param onEmptySuccess () -> Unit: Function to handle the empty success state
     * @param onError (Error) -> Unit: Function to handle the error state
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    suspend fun catcher(
        onLoading: suspend () -> Unit = {},
        onSuccess: suspend (T) -> Unit = {},
        onEmptySuccess: suspend () -> Unit = {},
        onError: suspend (Error) -> Unit = {}
    ) {
        when (this) {
            is Loading -> onLoading()
            is Success -> onSuccess(data)
            is EmptySuccess -> onEmptySuccess()
            is Error -> onError(this)
        }
    }

    /**
     * Loading state
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    data object Loading : ResponseState<Nothing>()

    /**
     * Success state
     *
     * @param data [T]: Data of the response
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    data class Success<out T>(
        val data: T
    ) : ResponseState<T>()

    /**
     * Empty success state
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    data object EmptySuccess : ResponseState<Nothing>()

    /**
     * Error state
     *
     * @param classLogData [es.sebas1705.common.managers.ClassLogData]: Class to log the error
     * @param type [ErrorResponseType]: Type of the error
     * @param message [String]: Message of the error
     * @param loggerAction (ClassLogData, String) -> Unit: Function to log the error
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    data class Error(
        val classLogData: ClassLogData,
        val type: ErrorResponseType,
        val message: String,
        private val loggerAction: (ClassLogData, String) -> Unit
    ) : ResponseState<Nothing>() {
        init {
            loggerAction(classLogData, "${type.tag}: $message")
        }
    }
}