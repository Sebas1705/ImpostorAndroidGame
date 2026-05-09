package es.sebas1705.main.viewmodel

import es.sebas1705.common.mvi.MVIBaseIntent

/**
 * Sealed interface that represents the possible intents of the Main Screen.
 *
 * @property ChargeData [MainIntent.ChargeData]: Intent to charge the data of the Main Screen.
 *
 * @see MVIBaseIntent
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
sealed interface MainIntent : MVIBaseIntent {

    data object ChargeData : MainIntent
}