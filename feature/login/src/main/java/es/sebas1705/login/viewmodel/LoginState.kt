package es.sebas1705.login.viewmodel

import es.sebas1705.common.mvi.MVIBaseState

data class LoginState(
    val isCheckingSession: Boolean = false,
    val navigateToHome: Boolean = false,
    val errorMessage: String? = null
) : MVIBaseState

