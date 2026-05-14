package es.sebas1705.authentication

import es.sebas1705.repositories.interfaces.IAuthenticationRepository
import javax.inject.Inject

/**
 * Checks if there is an authenticated Firebase user in the current session.
 */
class IsUserLoggedUseCase @Inject constructor(
    private val authenticationRepository: IAuthenticationRepository
) {
    suspend operator fun invoke(): Boolean {
        if (authenticationRepository.isUserLogged())
            return true

        // Fallback for devices where Firebase local auth state cannot be decrypted by keystore.
        return authenticationRepository.isSessionExpected()
    }
}

