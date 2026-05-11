package es.sebas1705.authenticationusescases

import es.sebas1705.repositories.interfaces.IAuthenticationRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Checks if there is an authenticated Firebase user in the current session.
 */
class IsUserLoggedUseCase @Inject constructor(
    private val authenticationRepository: IAuthenticationRepository
) {
    suspend operator fun invoke(): Boolean {
        repeat(MAX_RETRIES) {
            if (authenticationRepository.isUserLogged()) {
                return true
            }
            delay(RETRY_DELAY_MS)
        }

        // Fallback for devices where Firebase local auth state cannot be decrypted by keystore.
        return authenticationRepository.isSessionExpected()
    }

    private companion object {
        const val MAX_RETRIES = 8
        const val RETRY_DELAY_MS = 250L
    }
}
