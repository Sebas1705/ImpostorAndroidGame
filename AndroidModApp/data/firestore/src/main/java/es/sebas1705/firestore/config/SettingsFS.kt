package es.sebas1705.firestore.config

import es.sebas1705.firestore.config.SettingsFS.ERROR_GENERIC_MESSAGE_EX
import es.sebas1705.firestore.config.SettingsFS.ERROR_GENERIC_MESSAGE_FAIL


/**
 * Settings for Firestore
 *
 * @property ERROR_GENERIC_MESSAGE_EX [String]: Generic error message for exceptions
 * @property ERROR_GENERIC_MESSAGE_FAIL [String]: Generic error message for failure listeners
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
object SettingsFS {

    const val ERROR_GENERIC_MESSAGE_EX = "An error occurred on firestore by an exception"
    const val ERROR_GENERIC_MESSAGE_FAIL = "An error occurred on firestore by failure listener"
}