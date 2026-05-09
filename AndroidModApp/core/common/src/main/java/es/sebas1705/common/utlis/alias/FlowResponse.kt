package es.sebas1705.common.utlis.alias

import es.sebas1705.common.responses.ResponseState
import kotlinx.coroutines.flow.Flow

/**
 * Type alias to represent a flow of response
 *
 * @param T: Type of the response
 *
 * @see ResponseState
 * @see Flow
 */
typealias FlowResponse<T> = Flow<ResponseState<T>>