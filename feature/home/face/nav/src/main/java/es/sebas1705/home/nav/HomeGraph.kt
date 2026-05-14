package es.sebas1705.home.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

interface HomeGraph : NavKey {
    @Serializable
    object FaceScreen : HomeGraph

    @Serializable
    object RankingScreen : HomeGraph

    @Serializable
    object ProfileScreen : HomeGraph
}

