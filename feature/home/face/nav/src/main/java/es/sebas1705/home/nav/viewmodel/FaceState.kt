package es.sebas1705.home.nav.viewmodel

import es.sebas1705.common.mvi.MVIBaseState
import es.sebas1705.models.Categories
import es.sebas1705.models.Modes
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

data class FaceState(
    val categoriesStates: ImmutableMap<Categories, Boolean> = persistentMapOf(),
    val users: ImmutableList<String> = persistentListOf(),
    val mode: Modes = Modes.Classic,
    val impostors: Int = 1,
    val showImpostorsInResult: Boolean = true,
    val errorMessage: String? = null
) : MVIBaseState

