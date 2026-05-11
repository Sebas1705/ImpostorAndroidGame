package es.sebas1705.debug.models

import kotlinx.collections.immutable.ImmutableList

internal data class DebugToolsViewData(
    val metrics: ImmutableList<MetricCardData>,
    val selectedLanguage: String,
    val latestWord: String?,
    val topCategory: String?,
    val isLoading: Boolean,
    val errorMessage: String?
)

