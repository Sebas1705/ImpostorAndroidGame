package es.sebas1705.authentication.config

import android.content.Context
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption

private const val DEFAULT_WEB_CLIENT_ID_RES = "default_web_client_id"

private fun Context.resolveServerClientId(): String {
    val resourceId = resources.getIdentifier(
        DEFAULT_WEB_CLIENT_ID_RES,
        "string",
        packageName
    )
    return if (resourceId != 0) getString(resourceId)
    else SettingsAuth.FALLBACK_SERVER_CLIENT_ID
}

/**
 * Extension function that get the Google credential request
 *
 * @receiver [Context]
 *
 * @return [GetCredentialRequest]
 *
 * @see GetCredentialRequest
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
val Context.getCredentialRequestGoogle: GetCredentialRequest
    get() = GetCredentialRequest.Builder()
        .addCredentialOption(
            GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(SettingsAuth.FILTER_BY_AUTHORIZED_ACCOUNTS)
                .setServerClientId(resolveServerClientId())
                .build()
        )
        .build()
