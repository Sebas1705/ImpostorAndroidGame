package es.sebas1705.authenticationusescases

import es.sebas1705.repositories.interfaces.IAuthenticationRepository
import javax.inject.Inject

/**
 * Persists app-level expectation about user session state.
 */
class SetSessionExpectedUseCase @Inject constructor(
    private val authenticationRepository: IAuthenticationRepository
) {
    suspend operator fun invoke(value: Boolean) {
        authenticationRepository.setSessionExpected(value)
    }
}

