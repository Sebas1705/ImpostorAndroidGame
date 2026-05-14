package es.sebas1705.common.utlis.alias

import es.sebas1705.common.responses.ResponseState
import kotlinx.coroutines.flow.Flow

/**
 * Type alias to represent a flow of response with a Nothing type
 *
 * @see ResponseState
 * @see Flow
 */
typealias FlowResponseNothing = Flow<ResponseState<Nothing>>