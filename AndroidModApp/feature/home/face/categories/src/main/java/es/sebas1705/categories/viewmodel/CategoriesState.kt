package es.sebas1705.categories.viewmodel

import es.sebas1705.common.mvi.MVIBaseState
import es.sebas1705.models.Categories
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap

data class CategoriesState(
    val temp: String = "",
) : MVIBaseState

