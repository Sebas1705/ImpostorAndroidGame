package es.sebas1705.authentication.config

import android.content.Context
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption

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
                .setServerClientId("875884945428-k0hdf0jcctbne94ors1khudputut8klj.apps.googleusercontent.com")
                .build()
        )
        .build()
