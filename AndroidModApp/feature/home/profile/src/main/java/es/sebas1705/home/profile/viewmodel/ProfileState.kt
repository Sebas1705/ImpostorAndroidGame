package es.sebas1705.home.profile.viewmodel

import es.sebas1705.common.mvi.MVIBaseState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ProfileState(
    val navigateToLogin: Boolean = false,
    val errorMessage: String? = null,
    val rows: ImmutableList<String> = persistentListOf()
) : MVIBaseState

