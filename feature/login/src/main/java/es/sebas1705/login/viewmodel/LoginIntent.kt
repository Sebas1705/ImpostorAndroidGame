package es.sebas1705.login.viewmodel

import es.sebas1705.common.mvi.MVIBaseIntent

sealed interface LoginIntent : MVIBaseIntent {
    data object CheckSession : LoginIntent
    data object SignInWithGoogle : LoginIntent
    data object ConsumeNavigation : LoginIntent
    data object ClearError : LoginIntent
}

