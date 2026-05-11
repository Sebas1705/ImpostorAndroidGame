package es.sebas1705.authenticationusescases

import es.sebas1705.repositories.interfaces.IAuthenticationRepository
import javax.inject.Inject

/**
 * Closes Firebase user session.
 */
class SignOutUseCase @Inject constructor(
    private val authenticationRepository: IAuthenticationRepository,
    private val setSessionExpectedUseCase: SetSessionExpectedUseCase,
) {
    suspend operator fun invoke(): Boolean {
        val didSignOut = authenticationRepository.signOut()
        if (didSignOut) {
            setSessionExpectedUseCase(false)
        }
        return didSignOut
    }
}
