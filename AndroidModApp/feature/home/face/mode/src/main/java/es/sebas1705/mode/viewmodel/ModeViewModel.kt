package es.sebas1705.mode.viewmodel

import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.common.mvi.MVIBaseViewModel
import es.sebas1705.game.UpdateGameModeUseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ModeViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val updateGameModeUseCase: UpdateGameModeUseCase
) : MVIBaseViewModel<ModeState, ModeIntent>(context) {

    override fun initState(): ModeState = ModeState()

    override fun intentHandler(intent: ModeIntent) {
        when (intent) {
            is ModeIntent.Save -> save(intent)
        }
    }

    private fun save(
        intent: ModeIntent.Save
    ) = execute(Dispatchers.IO) {
        updateGameModeUseCase(
            intent.mode,
            intent.impostors,
            intent.showImpostorsInResult
        )
    }
}

