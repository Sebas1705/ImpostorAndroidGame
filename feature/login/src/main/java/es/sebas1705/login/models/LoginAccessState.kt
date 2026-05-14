package es.sebas1705.login.models

internal data class LoginAccessState(
    val isCheckingSession: Boolean,
    val loading: Boolean,
    val errorMessage: String?
)

