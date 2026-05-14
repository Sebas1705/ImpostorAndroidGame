package es.sebas1705.debug.models

import kotlinx.collections.immutable.ImmutableList

internal data class DebugTableData(
    val headers: ImmutableList<String>,
    val rows: ImmutableList<ImmutableList<String>>
)

