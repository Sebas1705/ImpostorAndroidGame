package es.sebas1705.login.viewmodel

import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.authenticationusescases.IsUserLoggedUseCase
import es.sebas1705.authenticationusescases.SetSessionExpectedUseCase
import es.sebas1705.authenticationusescases.SignInWithGoogleUseCase
import es.sebas1705.common.mvi.MVIBaseViewModel
import es.sebas1705.common.utlis.extensions.types.catcher
import es.sebas1705.common.utlis.extensions.types.logI
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val isUserLoggedUseCase: IsUserLoggedUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val setSessionExpectedUseCase: SetSessionExpectedUseCase,
    @ApplicationContext context: Context
) : MVIBaseViewModel<LoginState, LoginIntent>(context) {

    override fun initState(): LoginState = LoginState()

    override fun intentHandler(intent: LoginIntent) {
        when (intent) {
            LoginIntent.CheckSession -> checkSession()
            LoginIntent.SignInWithGoogle -> signInWithGoogle()
            LoginIntent.ConsumeNavigation -> consumeNavigation()
            LoginIntent.ClearError -> clearError()
        }
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
                updateUi {
                    it.copy(
                        navigateToHome = true,
                        errorMessage = null
                    )
                }
            },
            onError = { message ->
                stopLoading()
                logI("Google sign-in failed. message=$message")
                updateUi { it.copy(errorMessage = message) }
            }
        )
    }

    private fun clearError() = execute {
        updateUi { it.copy(errorMessage = null) }
    }

    private fun consumeNavigation() = execute {
        updateUi { it.copy(navigateToHome = false) }
    }
}
