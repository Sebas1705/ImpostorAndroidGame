package es.sebas1705.debug.viewmodel

import android.content.Context
import android.os.SystemClock
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.common.mvi.MVIBaseViewModel
import es.sebas1705.core.resources.R
import es.sebas1705.game.debug.GetDeviceDebugMetricsUseCase
import es.sebas1705.game.debug.GetPerformanceDebugMetricsUseCase
import es.sebas1705.game.debug.ReadDebugSnapshotsUseCase
import es.sebas1705.game.debug.SaveDebugSnapshotUseCase
import es.sebas1705.game.words.GetWordsDbStatsUseCase
import es.sebas1705.game.words.ResetDefaultWordsUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class DebugToolsViewModel @Inject constructor(
    private val getWordsDbStatsUseCase: GetWordsDbStatsUseCase,
    private val getDeviceDebugMetricsUseCase: GetDeviceDebugMetricsUseCase,
    private val getPerformanceDebugMetricsUseCase: GetPerformanceDebugMetricsUseCase,
    private val saveDebugSnapshotUseCase: SaveDebugSnapshotUseCase,
    private val readDebugSnapshotsUseCase: ReadDebugSnapshotsUseCase,
    private val resetDefaultWordsUseCase: ResetDefaultWordsUseCase,
    @ApplicationContext context: Context
) : MVIBaseViewModel<DebugToolsUiState, DebugToolsIntent>(context) {

    override fun initState(): DebugToolsUiState = DebugToolsUiState()

    override fun intentHandler(intent: DebugToolsIntent): Job =
        when (intent) {
            DebugToolsIntent.RefreshAll -> refreshAll()
            DebugToolsIntent.ResetDefaultWords -> resetDefaultWords()
            is DebugToolsIntent.SelectTab -> selectTab(intent.tab)
        }

    private fun selectTab(tab: DebugMetricsTab): Job = execute {
        updateUi { it.copy(selectedTab = tab) }
    }

    @Suppress("LongMethod")
    private fun refreshAll(): Job = execute(Dispatchers.IO) {
        startLoading()
        updateUi { it.copy(errorMessage = null) }

        val startedAt = SystemClock.elapsedRealtime()

        runCatching {
            val stats = getWordsDbStatsUseCase()
            val refreshDurationMs = SystemClock.elapsedRealtime() - startedAt
            saveDebugSnapshotUseCase(stats)
            val snapshots = readDebugSnapshotsUseCase()
            val device = getDeviceDebugMetricsUseCase()
            val performance = getPerformanceDebugMetricsUseCase(
                refreshDurationMs = refreshDurationMs,
                resetDurationMs = uiState.value.resetDurationMs
            )

            CombinedDiagnostics(
                stats = stats,
                refreshDurationMs = performance.refreshDurationMs,
                appUptimeMs = performance.appUptimeMs,
                usedHeapMb = performance.usedHeapMb,
                maxHeapMb = performance.maxHeapMb,
                manufacturer = device.manufacturer,
                model = device.model,
                androidVersion = device.androidVersion,
                sdkInt = device.sdkInt,
                totalRamMb = device.totalRamMb,
                availableRamMb = device.availableRamMb,
                isLowRamDevice = device.isLowRamDevice,
                localeTag = device.localeTag,
                snapshots = snapshots.toImmutableList()
            )
        }.onSuccess { diagnostics ->
            updateUi {
                it.copy(
                    selectedLanguage = diagnostics.stats.selectedLanguage,
                    totalWordsEs = diagnostics.stats.totalWordsEs,
                    totalWordsEn = diagnostics.stats.totalWordsEn,
                    selectedLanguageWords = diagnostics.stats.selectedLanguageWords,
                    categoriesEs = diagnostics.stats.categoriesEs,
                    categoriesEn = diagnostics.stats.categoriesEn,
                    categoryCoverageEs = diagnostics.stats.categoryCoverageEs,
                    categoryCoverageEn = diagnostics.stats.categoryCoverageEn,
                    categoryCoverageSelectedLanguage = diagnostics.stats.categoryCoverageSelectedLanguage,
                    averageCluesInSelectedLanguage = diagnostics.stats.averageCluesInSelectedLanguage,
                    duplicateWordsInSelectedLanguage = diagnostics.stats.duplicateWordsInSelectedLanguage,
                    pureDuplicateEntriesInSelectedLanguage =
                        diagnostics.stats.pureDuplicateEntriesInSelectedLanguage,
                    invalidClueEntriesInSelectedLanguage =
                        diagnostics.stats.invalidClueEntriesInSelectedLanguage,
                    missingCategoriesInSelectedLanguage =
                        diagnostics.stats.missingCategoriesInSelectedLanguage,
                    validClueEntriesCoverageInSelectedLanguage =
                        diagnostics.stats.validClueEntriesCoverageInSelectedLanguage,
                    refreshDurationMs = diagnostics.refreshDurationMs,
                    appUptimeMs = diagnostics.appUptimeMs,
                    usedHeapMb = diagnostics.usedHeapMb,
                    maxHeapMb = diagnostics.maxHeapMb,
                    manufacturer = diagnostics.manufacturer,
                    model = diagnostics.model,
                    androidVersion = diagnostics.androidVersion,
                    sdkInt = diagnostics.sdkInt,
                    totalRamMb = diagnostics.totalRamMb,
                    availableRamMb = diagnostics.availableRamMb,
                    isLowRamDevice = diagnostics.isLowRamDevice,
                    localeTag = diagnostics.localeTag,
                    snapshots = diagnostics.snapshots,
                    latestWordInSelectedLanguage = diagnostics.stats.latestWordInSelectedLanguage,
                    topCategoryInSelectedLanguage = diagnostics.stats.topCategoryInSelectedLanguage,
                    errorMessage = null
                )
            }
        }.onFailure { throwable ->
            updateUi {
                it.copy(
                    errorMessage = throwable.message
                        ?: context.getString(R.string.core_resources_debug_error_unknown_diagnostics)
                )
            }
        }

        stopLoading()
    }

    private fun resetDefaultWords(): Job = execute(Dispatchers.IO) {
        startLoading()
        updateUi { it.copy(errorMessage = null) }

        val startedAt = SystemClock.elapsedRealtime()

        runCatching { resetDefaultWordsUseCase() }
            .onSuccess {
                val durationMs = SystemClock.elapsedRealtime() - startedAt
                updateUi { it.copy(resetDurationMs = durationMs) }
            }
            .onFailure { throwable ->
                updateUi {
                    it.copy(
                        errorMessage = throwable.message
                            ?: context.getString(R.string.core_resources_debug_error_import_default_words)
                    )
                }
            }

        refreshAll()
    }

    private data class CombinedDiagnostics(
        val stats: es.sebas1705.models.WordsDbStatsModel,
        val refreshDurationMs: Long,
        val appUptimeMs: Long,
        val usedHeapMb: Long,
        val maxHeapMb: Long,
        val manufacturer: String,
        val model: String,
        val androidVersion: String,
        val sdkInt: Int,
        val totalRamMb: Long,
        val availableRamMb: Long,
        val isLowRamDevice: Boolean,
        val localeTag: String,
        val snapshots: kotlinx.collections.immutable.ImmutableList<es.sebas1705.models.DebugSnapshotModel>
    )
}