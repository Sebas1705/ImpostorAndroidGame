package es.sebas1705.mvisample.viewmodel

import es.sebas1705.common.mvi.MVIBaseState

/**
 * MviSampleState
 *
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
data class MviSampleState(
    val helloWorld: String = "Hello World",
) : MVIBaseState