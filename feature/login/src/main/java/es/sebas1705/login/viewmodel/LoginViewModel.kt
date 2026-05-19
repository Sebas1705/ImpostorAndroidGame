package es.sebas1705.login.viewmodel

import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.authentication.IsEmailVerifiedUseCase
import es.sebas1705.authentication.IsUserLoggedUseCase
import es.sebas1705.authentication.SendPasswordResetUseCase
import es.sebas1705.authentication.SendVerificationEmailUseCase
import es.sebas1705.authentication.SetSessionExpectedUseCase
import es.sebas1705.authentication.SignInAsGuestUseCase
import es.sebas1705.authentication.SignInWithEmailUseCase
import es.sebas1705.authentication.SignInWithGoogleUseCase
import es.sebas1705.authentication.SignUpWithEmailUseCase
import es.sebas1705.common.mvi.MVIBaseViewModel
import es.sebas1705.common.utlis.extensions.types.catcher
import es.sebas1705.common.utlis.extensions.types.logI
import es.sebas1705.core.resources.R
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
@Suppress("TooManyFunctions")
class LoginViewModel @Inject constructor(
    private val isUserLoggedUseCase: IsUserLoggedUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val signUpWithEmailUseCase: SignUpWithEmailUseCase,
    private val sendPasswordResetUseCase: SendPasswordResetUseCase,
    private val signInAsGuestUseCase: SignInAsGuestUseCase,
    private val sendVerificationEmailUseCase: SendVerificationEmailUseCase,
    private val isEmailVerifiedUseCase: IsEmailVerifiedUseCase,
    private val setSessionExpectedUseCase: SetSessionExpectedUseCase,
    @ApplicationContext context: Context
) : MVIBaseViewModel<LoginState, LoginIntent>(context) {

    override fun initState(): LoginState = LoginState()

    override fun intentHandler(intent: LoginIntent) =
        when (intent) {
            LoginIntent.CheckSession -> checkSession()
            LoginIntent.SignInWithGoogle -> signInWithGoogle()
            LoginIntent.SignInAsGuest -> signInAsGuest()
            is LoginIntent.SignInWithEmail -> signInWithEmail(intent.email, intent.password)
            is LoginIntent.SignUpWithEmail -> signUpWithEmail(intent.email, intent.password)
            is LoginIntent.SendPasswordReset -> sendPasswordReset(intent.email)
            LoginIntent.ResendVerificationEmail -> resendVerificationEmail()
            LoginIntent.CheckEmailVerified -> checkEmailVerified()
            is LoginIntent.ChangeMode -> changeMode(intent.mode)
            LoginIntent.ConsumeNavigation -> consumeNavigation()
            LoginIntent.ClearError -> clearError()
        }

    private fun checkSession() = execute(Dispatchers.IO) {
        updateUi { it.copy(isCheckingSession = true) }
        val isLogged = isUserLoggedUseCase()
        logI("Login checkSession result. isLogged=$isLogged")
        updateUi {
            it.copy(
                isCheckingSession = false,
                navigateToHome = isLogged,
                errorMessage = null
            )
        }
    }

    private fun signInWithGoogle() = execute(Dispatchers.IO) {
        signInWithGoogleUseCase().catcher(
            onLoading = { startLoading() },
            onEmptySuccess = {
                stopLoading()
                setSessionExpectedUseCase(true)
                logI("Google sign-in success. Navigating to Home")
                updateUi { it.copy(navigateToHome = true, errorMessage = null) }
            },
            onError = { message ->
                stopLoading()
                logI("Google sign-in failed. message=$message")
                updateUi { it.copy(errorMessage = message) }
            }
        )
    }

    private fun signInWithEmail(email: String, password: String) = execute(Dispatchers.IO) {
        signInWithEmailUseCase(email, password).catcher(
            onLoading = { startLoading() },
            onEmptySuccess = {
                stopLoading()
                if (isEmailVerifiedUseCase()) {
                    setSessionExpectedUseCase(true)
                    logI("Email sign-in success. Email verified. Navigating to Home")
                    updateUi { it.copy(navigateToHome = true, errorMessage = null) }
                } else {
                    logI("Email sign-in success but email not verified. Sending verification email.")
                    sendVerificationAndSignOut()
                }
            },
            onError = { message ->
                stopLoading()
                updateUi { it.copy(errorMessage = message) }
            }
        )
    }

    private fun signUpWithEmail(email: String, password: String) = execute(Dispatchers.IO) {
        signUpWithEmailUseCase(email, password).catcher(
            onLoading = { startLoading() },
            onEmptySuccess = {
                logI("Email sign-up success. Sending verification email.")
                sendVerificationAndSignOut()
            },
            onError = { message ->
                stopLoading()
                updateUi { it.copy(errorMessage = message) }
            }
        )
    }

    private suspend fun sendVerificationAndSignOut() {
        sendVerificationEmailUseCase().catcher(
            onLoading = {},
            onEmptySuccess = {
                stopLoading()
                logI("Verification email sent. Showing verification panel.")
                updateUi {
                    it.copy(
                        loginMode = LoginMode.EmailVerification,
                        isWaitingEmailVerification = true,
                        errorMessage = null
                    )
                }
            },
            onError = { message ->
                stopLoading()
                updateUi {
                    it.copy(
                        loginMode = LoginMode.EmailVerification,
                        isWaitingEmailVerification = true,
                        errorMessage = message
                    )
                }
            }
        )
    }

    private fun sendPasswordReset(email: String) = execute(Dispatchers.IO) {
        sendPasswordResetUseCase(email).catcher(
            onLoading = { startLoading() },
            onEmptySuccess = {
                stopLoading()
                logI("Password reset email sent to $email")
                updateUi { it.copy(resetEmailSent = true, errorMessage = null) }
            },
            onError = { message ->
                stopLoading()
                updateUi { it.copy(errorMessage = message) }
            }
        )
    }

    private fun resendVerificationEmail() = execute(Dispatchers.IO) {
        sendVerificationEmailUseCase().catcher(
            onLoading = { startLoading() },
            onEmptySuccess = {
                stopLoading()
                logI("Verification email resent.")
                updateUi { it.copy(errorMessage = null) }
            },
            onError = { message ->
                stopLoading()
                updateUi { it.copy(errorMessage = message) }
            }
        )
    }

    private fun checkEmailVerified() = execute(Dispatchers.IO) {
        updateUi { it.copy(errorMessage = null) }
        if (isEmailVerifiedUseCase()) {
            setSessionExpectedUseCase(true)
            logI("Email verified. Navigating to Home.")
            updateUi { it.copy(navigateToHome = true, isWaitingEmailVerification = false) }
        } else {
            logI("Email not verified yet.")
            updateUi {
                it.copy(errorMessage = context.getString(R.string.core_resources_login_email_not_verified))
            }
        }
    }

    private fun signInAsGuest() = execute(Dispatchers.IO) {
        signInAsGuestUseCase().catcher(
            onLoading = { startLoading() },
            onEmptySuccess = {
                stopLoading()
                logI("Guest sign-in success. Navigating to Home as guest.")
                updateUi { it.copy(navigateToHome = true, navigateAsGuest = true, errorMessage = null) }
            },
            onError = { message ->
                stopLoading()
                updateUi { it.copy(errorMessage = message) }
            }
        )
    }

    private fun changeMode(mode: LoginMode) = execute {
        updateUi { it.copy(loginMode = mode, errorMessage = null, resetEmailSent = false) }
    }

    private fun clearError() = execute {
        updateUi { it.copy(errorMessage = null) }
    }

    private fun consumeNavigation() = execute {
        updateUi { it.copy(navigateToHome = false, navigateAsGuest = false) }
    }
}
