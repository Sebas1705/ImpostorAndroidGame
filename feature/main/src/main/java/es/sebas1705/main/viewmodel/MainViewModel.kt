package es.sebas1705.main.viewmodel

import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.authentication.IsUserLoggedUseCase
import es.sebas1705.common.mvi.MVIBaseViewModel
import es.sebas1705.common.utlis.extensions.types.logI
import es.sebas1705.game.words.ImportDefaultWordsUseCase
import es.sebas1705.settings.ReadSettingsUseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * ViewModel of the Main Screen.
 *
 * @param context [Context]: Application context to access system services and resources.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val isUserLoggedUseCase: IsUserLoggedUseCase,
    private val readSettingsUseCase: ReadSettingsUseCase,
    private val importDefaultWordsUseCase: ImportDefaultWordsUseCase,
    @ApplicationContext context: Context
) : MVIBaseViewModel<MainState, MainIntent>(context) {

    init {
        execute(Dispatchers.IO) {
            readSettingsUseCase().collect { settings ->
                updateUi {
                    it.copy(
                        appLanguage = settings.appLanguage,
                        themeContrast = settings.appContrast,
                        forceCompactTables = settings.forceCompactTables,
                    )
                }
            }
        }
    }

    override fun initState(): MainState = MainState()

    override fun intentHandler(intent: MainIntent) =
        when (intent) {
            is MainIntent.ChargeData -> chargeData()
        }


    //Actions:
    /**
     * Action associated with [MainIntent.ChargeData] that will charge the data of the Main Screen.
     *
     * @see [MainIntent.ChargeData]
     */
    private fun chargeData() = execute(Dispatchers.IO) {
        importDefaultWordsUseCase()
        val isUserLogged = isUserLoggedUseCase()
        logI("Splash auth decision. isUserLogged=$isUserLogged")
        updateUi {
            it.copy(
                splashFinished = true,
                isUserLogged = isUserLogged
            )
        }
    }


}