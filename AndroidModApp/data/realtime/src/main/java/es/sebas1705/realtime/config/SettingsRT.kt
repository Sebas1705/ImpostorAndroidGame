package es.sebas1705.realtime.config

import es.sebas1705.realtime.config.SettingsRT.ERROR_GENERIC_MESSAGE_EX
import es.sebas1705.realtime.config.SettingsRT.ERROR_GENERIC_MESSAGE_FAIL


/**
 * Realtime references used in the app
 *
 * @property ERROR_GENERIC_MESSAGE_EX [String]: Generic error message by exception
 * @property ERROR_GENERIC_MESSAGE_FAIL [String]: Generic error message by failure listener
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
object SettingsRT {

    const val ERROR_GENERIC_MESSAGE_EX = "An error occurred on realtime by an exception"
    const val ERROR_GENERIC_MESSAGE_FAIL = "An error occurred on realtime by failure listener"
}

