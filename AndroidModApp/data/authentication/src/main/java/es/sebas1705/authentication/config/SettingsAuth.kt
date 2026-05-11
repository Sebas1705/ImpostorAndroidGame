package es.sebas1705.authentication.config


/**
 * Settings of the authentication
 *
 * @property FILTER_BY_AUTHORIZED_ACCOUNTS [Boolean]: Filter by authorized accounts
 * @property ERROR_GENERIC_MESSAGE_EX [String]: Error message by exception
 * @property ERROR_GENERIC_MESSAGE_FAIL [String]: Error message by failure listener
 * @property NOT_LOGGED_USER [String]: Not logged user message
 * @property USER_NOT_OUT [String]: User not logged out message
 * @property WRONG_CREDENTIALS [String]: Wrong credentials message
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
object SettingsAuth {
    const val FILTER_BY_AUTHORIZED_ACCOUNTS = false

    const val ERROR_GENERIC_MESSAGE_EX = "An error occurred on authentication by an exception"
    const val ERROR_GENERIC_MESSAGE_FAIL = "An error occurred on authentication by failure listener"
    const val ERROR_NO_CREDENTIALS = "No Google credentials available on this device. Add a Google account and try again."

    // Fallback used only when default_web_client_id is not present in resources.
    const val FALLBACK_SERVER_CLIENT_ID =
        "875884945428-k0hdf0jcctbne94ors1khudputut8klj.apps.googleusercontent.com"

    const val NOT_LOGGED_USER = "Not correctly logged user to take their data"
    const val USER_NOT_OUT = "User not logged out"
    const val WRONG_CREDENTIALS = "Wrong credentials"
}