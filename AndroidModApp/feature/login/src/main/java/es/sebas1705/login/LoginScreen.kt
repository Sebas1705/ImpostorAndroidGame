package es.sebas1705.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.sebas1705.login.design.LoginDesign
import es.sebas1705.login.viewmodel.LoginIntent
import es.sebas1705.login.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit = {},
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
    val loading by loginViewModel.loading.collectAsStateWithLifecycle()

    LaunchedEffect(null) {
        loginViewModel.eventHandler(LoginIntent.CheckSession)
    }

    LaunchedEffect(uiState.navigateToHome, onLoginSuccess) {
        if (uiState.navigateToHome) {
            onLoginSuccess()
            loginViewModel.eventHandler(LoginIntent.ConsumeNavigation)
        }
    }

    LoginDesign(
        modifier = modifier,
        isCheckingSession = uiState.isCheckingSession,
        loading = loading,
        errorMessage = uiState.errorMessage,
        onGoogleSignIn = { loginViewModel.eventHandler(LoginIntent.SignInWithGoogle) },
        onDismissError = { loginViewModel.eventHandler(LoginIntent.ClearError) }
    )
}

