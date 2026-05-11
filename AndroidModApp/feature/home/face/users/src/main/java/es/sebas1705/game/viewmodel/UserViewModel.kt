package es.sebas1705.game.viewmodel

import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.common.mvi.MVIBaseViewModel
import es.sebas1705.game.UpdateGamePlayersUseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val updateGamePlayersUseCase: UpdateGamePlayersUseCase,
) : MVIBaseViewModel<UserState, UserIntent>(context) {

    override fun initState(): UserState = UserState()

    override fun intentHandler(intent: UserIntent) {
        when (intent) {
            is UserIntent.Save -> save(intent)
        }
    }

    private fun save(
        intent: UserIntent.Save
    ) = execute(Dispatchers.IO) {
        updateGamePlayersUseCase(intent.playerNames)
    }
}

