package es.sebas1705.authentication.config

import android.content.Context
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class ExtensionsAuthTest {

    @Test
    fun `getCredentialRequestGoogle builds request`() {
        val context: Context = mock(Context::class.java)

        val request: GetCredentialRequest = context.getCredentialRequestGoogle

        assertNotNull(request)
        assertEquals(1, request.credentialOptions.size)
        assertEquals(SettingsAuth.FILTER_BY_AUTHORIZED_ACCOUNTS, (request.credentialOptions[0] as GetGoogleIdOption).filterByAuthorizedAccounts)
    }
}
