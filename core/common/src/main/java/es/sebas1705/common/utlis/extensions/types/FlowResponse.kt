package es.sebas1705.common.utlis.extensions.types

import es.sebas1705.common.utlis.alias.FlowResponse
import es.sebas1705.common.utlis.alias.FlowResponseNothing

/**
 * Catcher function to handle the different states of the response
 *
 * @receiver [FlowResponse]: the response state
 *
 * @param onLoading () -> Unit: Function to handle the loading state
 * @param onSuccess (T) -> Unit: Function to handle the success state
 * @param onError (String) -> Unit: Function to handle the error state
 *
 * @see FlowResponse
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
suspend fun <T> FlowResponse<T>.catcher(
    onLoading: suspend () -> Unit = {},
    onSuccess: suspend (T) -> Unit = {},
    onError: suspend (String) -> Unit = {}
) {
    this.collect { response ->
        response.catcher(
            onLoading = onLoading,
            onSuccess = onSuccess,
            onError = { onError(it.message) }
        )
    }
}

/**
 * Catcher function to handle the different states of the response
 *
 * @receiver [FlowResponseNothing]: the response state
 *
 * @param onLoading () -> Unit: Function to handle the loading state
 * @param onEmptySuccess () -> Unit: Function to handle the empty success state
 * @param onError (String) -> Unit: Function to handle the error state
 *
 * @see FlowResponseNothing
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
suspend fun FlowResponseNothing.catcher(
    onLoading: suspend () -> Unit = {},
    onEmptySuccess: suspend () -> Unit = {},
    onError: suspend (String) -> Unit = {}
) {
    this.collect { response ->
        response.catcher(
            onLoading = onLoading,
            onEmptySuccess = onEmptySuccess,
            onError = { onError(it.message) }
        )
    }
}