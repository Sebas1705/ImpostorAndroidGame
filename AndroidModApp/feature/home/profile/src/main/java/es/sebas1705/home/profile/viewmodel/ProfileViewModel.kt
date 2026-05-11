package es.sebas1705.home.profile.viewmodel

import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.core.resources.R
import es.sebas1705.authenticationusescases.SignOutUseCase
import es.sebas1705.common.mvi.MVIBaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    @ApplicationContext context: Context
) : MVIBaseViewModel<ProfileState, ProfileIntent>(context) {

    private val appContext: Context = context

    override fun initState(): ProfileState = ProfileState(
        rows = listOf(
            appContext.getString(
                R.string.core_resources_profile_row_role_preference,
                appContext.getString(R.string.core_resources_profile_role_impostor_hunter)
            ),
            appContext.getString(
                R.string.core_resources_profile_row_favorite_category,
                appContext.getString(R.string.core_resources_profile_category_space_astronomy)
            ),
            appContext.getString(R.string.core_resources_profile_row_matches_played, 67),
            appContext.getString(R.string.core_resources_profile_row_best_streak, 9)
        ).toImmutableList()
    )

    override fun intentHandler(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.Load -> Unit
            ProfileIntent.SignOut -> signOut()
            ProfileIntent.ConsumeSignOutNavigation -> consumeSignOutNavigation()
        }
    }

    private fun signOut() = execute(Dispatchers.IO) {
        val isSuccess = signOutUseCase()
        updateUi {
            if (isSuccess) {
                it.copy(navigateToLogin = true, errorMessage = null)
            } else {
                it.copy(errorMessage = appContext.getString(R.string.core_resources_settings_sign_out_error))
            }
        }
    }

    private fun consumeSignOutNavigation() = execute {
        updateUi { it.copy(navigateToLogin = false) }
    }
}

