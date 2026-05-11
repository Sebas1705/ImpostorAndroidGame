package es.sebas1705.repositories.interfaces

import es.sebas1705.common.utlis.alias.FlowResponseNothing

/**
 * Repository interface to authenticate the user
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
interface IAuthenticationRepository {

    //Tasks:
    /**
     * Sign up with email and password
     *
     * @param email [String]: Email to sign up
     * @param password [String]: Password to sign up
     *
     * @return [FlowResponseNothing]: with the response of the operation
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun signUpWithEmail(
        email: String,
        password: String
    ): FlowResponseNothing

    /**
     * Sign in with email and password
     *
     * @param email [String]: Email to sign in
     * @param password [String]: Password to sign in
     *
     * @return [FlowResponseNothing]: with the response of the operation
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun signInWithEmail(
        email: String,
        password: String
    ): FlowResponseNothing

    /**
     * Sign in with Google
     *
     * @return [FlowResponseNothing]: with the response of the operation
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    suspend fun signWithGoogle(): FlowResponseNothing

    /**
     * Send a forgot password email
     *
     * @param email [String]: Email to send the forgot password email
     *
     * @return [FlowResponseNothing]: with the response of the operation
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun sendForgotPassword(email: String): FlowResponseNothing

    //Functions:

    /**
     * Sign out the user
     *
     * @return [Boolean] with the result
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun signOut(): Boolean

    /**
     * Check if the user is logged
     *
     * @return [Boolean] with the result
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun isUserLogged(): Boolean

    /**
     * Reads whether app expects an authenticated user in this installation.
     */
    suspend fun isSessionExpected(): Boolean

    /**
     * Persists whether app should expect an authenticated user in this installation.
     */
    suspend fun setSessionExpected(value: Boolean)

}