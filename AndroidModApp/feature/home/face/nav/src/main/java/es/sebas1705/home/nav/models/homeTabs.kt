package es.sebas1705.home.nav.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material.icons.outlined.SportsEsports
import es.sebas1705.home.nav.HomeGraph

internal val homeTabs = listOf(
    HomeTab(HomeGraph.FaceScreen, "Play", Icons.Outlined.SportsEsports),
    HomeTab(HomeGraph.RankingScreen, "Ranking", Icons.Outlined.EmojiEvents),
    HomeTab(HomeGraph.ProfileScreen, "Profile", Icons.Outlined.ManageAccounts)
)