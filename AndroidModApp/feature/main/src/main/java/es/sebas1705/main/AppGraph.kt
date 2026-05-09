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
    object MvvmSampleScreen : AppGraph

    @Serializable
    object MVISampleScreen : AppGraph

    @Serializable
    object DebugToolsScreen : AppGraph
}




