package es.sebas1705.authentication

import es.sebas1705.repositories.interfaces.IAuthenticationRepository
import javax.inject.Inject

/**
 * Snapshot of the authenticated user's Google profile fields.
 *
 * All fields may be null when no user is signed in or when the provider
 * did not supply the value.
 */
data class UserProfileData(
    val displayName: String?,
    val email: String?,
    val photoUrl: String?,
)

/**
 * Returns a one-shot [UserProfileData] from the currently authenticated Firebase user.
 * Does **not** emit updates — call it once per load cycle.
 *
 * @since 0.1.0
 */
class GetUserProfileDataUseCase @Inject constructor(
    private val authenticationRepository: IAuthenticationRepository,
) {
    operator fun invoke(): UserProfileData = UserProfileData(
        displayName = authenticationRepository.getUserDisplayName(),
        email = authenticationRepository.getUserEmail(),
        photoUrl = authenticationRepository.getUserPhotoUrl(),
    )
}
