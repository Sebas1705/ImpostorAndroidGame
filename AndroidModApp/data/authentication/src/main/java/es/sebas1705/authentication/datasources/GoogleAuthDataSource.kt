package es.sebas1705.authentication.datasources

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.analytics.datasources.LogEventDataSource
import es.sebas1705.authentication.config.SettingsAuth
import es.sebas1705.authentication.config.getCredentialRequestGoogle
import es.sebas1705.common.managers.ClassLogData
import es.sebas1705.common.managers.TaskFlowManager
import es.sebas1705.common.responses.ErrorResponseType
import es.sebas1705.common.responses.ResponseState
import es.sebas1705.common.utlis.alias.FlowResponseNothing
import es.sebas1705.common.utlis.extensions.types.logE
import es.sebas1705.common.utlis.extensions.types.logI
import javax.inject.Inject

/**
 * Authentication repository implementation
 *
 * @property credentialManager [CredentialManager]: credential manager to get the google credential
 * @property firebaseAuth [FirebaseAuth]: firebase authentication instance
 * @property logEventDataSource [LogEventDataSource]: data source for logging events
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class GoogleAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val logEventDataSource: LogEventDataSource,
    @param:ApplicationContext private val context: Context
) : ClassLogData() {

    //Properties:
    private var credentialManager: CredentialManager? = null

    //Managers:
    private val taskFlowManager = TaskFlowManager(
        this,
        logEventDataSource::logError,
        SettingsAuth.ERROR_GENERIC_MESSAGE_FAIL,
        SettingsAuth.ERROR_GENERIC_MESSAGE_EX
    )

    //Tasks:
    /**
     * Signs in with Google using the provided context.
     *
     * @return [FlowResponseNothing]: A flow response indicating the result of the sign-in operation.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    suspend fun signWithGoogle(): FlowResponseNothing {
        credentialManager = CredentialManager.create(context)
        var credential: Credential? = null
        var error = ""
        try {
            credential = credentialManager!!.getCredential(
                context,
                context.getCredentialRequestGoogle
            ).credential
        } catch (e: NoCredentialException) {
            logE("No credentials available: ${e.message}")
            error = SettingsAuth.ERROR_NO_CREDENTIALS
        } catch (e: Exception) {
            logE("Error getting google credential: ${e.message}")
            error = e.message ?: SettingsAuth.ERROR_GENERIC_MESSAGE_FAIL
        }
        return taskFlowManager.taskFlowProducer(
            assertChecker = {
                when (credential) {
                    null -> error
                    is CustomCredential if credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                        -> null

                    else -> SettingsAuth.WRONG_CREDENTIALS
                }
            },
            taskAction = {
                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(credential!!.data)
                val authCredential =
                    GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                firebaseAuth.signInWithCredential(authCredential)
            },
            onSuccessListener = {
                if (it.user != null) {
                    logI("Google auth success. Firebase uid=${maskUid(it.user!!.uid)}")
                    ResponseState.EmptySuccess
                }
                else taskFlowManager.createResponse(
                    ErrorResponseType.INTERNAL,
                    SettingsAuth.NOT_LOGGED_USER
                )
            }
        )
    }

    private fun maskUid(uid: String): String =
        if (uid.length <= 6) uid else "${uid.take(3)}...${uid.takeLast(3)}"
}