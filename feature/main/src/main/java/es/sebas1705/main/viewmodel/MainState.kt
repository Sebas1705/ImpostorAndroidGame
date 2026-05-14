package es.sebas1705.main.viewmodel

import es.sebas1705.common.mvi.MVIBaseState
import es.sebas1705.common.theme.ThemeContrast
import es.sebas1705.models.AppLanguage

/**
 * Data class that represents the state of the Main Screen.
 *
 * @param splashFinished [Boolean]: Indicates whether the splash screen has finished or not.
 * @see MVIBaseState
 *
 * @since 0.1.0
 * @author Sebas1705 05/07/2025
 */
data class MainState(
    val splashFinished: Boolean = false,
    val isUserLogged: Boolean = false,
    val appLanguage: AppLanguage = AppLanguage.English,
    val themeContrast: ThemeContrast = ThemeContrast.Low,
    val forceCompactTables: Boolean = false,
) : MVIBaseState