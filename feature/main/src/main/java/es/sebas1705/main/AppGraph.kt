package es.sebas1705.main

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Screens of the app.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
interface AppGraph: NavKey{

    @Serializable
    object SplashScreen : AppGraph

    @Serializable
    object LoginScreen : AppGraph

    @Serializable
    object HomeScreen : AppGraph

    @Serializable
    data class OfflineGameScreen(
        val players: List<String>,
        val categories: List<String>,
        val modeName: String,
        val impostors: Int,
        val showImpostorsInResult: Boolean,
        val discussionTimerSeconds: Int,
        val impostorsKnowEachOther: Boolean,
        val showNumOfImpostors: Boolean
    ) : AppGraph



    @Serializable
    object DebugToolsScreen : AppGraph
}




