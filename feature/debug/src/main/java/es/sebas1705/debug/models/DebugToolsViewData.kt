package es.sebas1705.debug.models

import kotlinx.collections.immutable.ImmutableList

internal data class DebugToolsViewData(
    val tabs: ImmutableList<String>,
    val selectedTabIndex: Int,
    val metrics: ImmutableList<MetricCardData>,
    val table: DebugTableData?,
    val selectedLanguage: String,
    val latestWord: String?,
    val topCategory: String?,
    val errorMessage: String?
)

