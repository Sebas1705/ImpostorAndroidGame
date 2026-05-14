package es.sebas1705.debug.viewmodel

import es.sebas1705.common.mvi.MVIBaseState
import es.sebas1705.models.DebugSnapshotModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DebugToolsUiState(
    val selectedTab: DebugMetricsTab = DebugMetricsTab.Data,
    val selectedLanguage: String = "en",
    val totalWordsEs: Int = 0,
    val totalWordsEn: Int = 0,
    val selectedLanguageWords: Int = 0,
    val categoriesEs: Int = 0,
    val categoriesEn: Int = 0,
    val categoryCoverageEs: Double = 0.0,
    val categoryCoverageEn: Double = 0.0,
    val categoryCoverageSelectedLanguage: Double = 0.0,
    val averageCluesInSelectedLanguage: Double = 0.0,
    val duplicateWordsInSelectedLanguage: Int = 0,
    val pureDuplicateEntriesInSelectedLanguage: Int = 0,
    val invalidClueEntriesInSelectedLanguage: Int = 0,
    val missingCategoriesInSelectedLanguage: Int = 0,
    val validClueEntriesCoverageInSelectedLanguage: Double = 0.0,
    val refreshDurationMs: Long = 0L,
    val resetDurationMs: Long = 0L,
    val appUptimeMs: Long = 0L,
    val usedHeapMb: Long = 0L,
    val maxHeapMb: Long = 0L,
    val manufacturer: String = "",
    val model: String = "",
    val androidVersion: String = "",
    val sdkInt: Int = 0,
    val totalRamMb: Long = 0L,
    val availableRamMb: Long = 0L,
    val isLowRamDevice: Boolean = false,
    val localeTag: String = "",
    val snapshots: ImmutableList<DebugSnapshotModel> = persistentListOf(),
    val latestWordInSelectedLanguage: String? = null,
    val topCategoryInSelectedLanguage: String? = null,
    val errorMessage: String? = null
) : MVIBaseState