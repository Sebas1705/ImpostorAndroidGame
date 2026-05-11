package es.sebas1705.debug

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.sebas1705.core.resources.R as ResourceR
import es.sebas1705.debug.models.DebugToolsActions
import es.sebas1705.debug.design.DebugToolsDesign
import es.sebas1705.debug.models.DebugToolsViewData
import es.sebas1705.debug.models.MetricCardData
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.roundToInt

@Composable
@Suppress("LongMethod")
fun DebugToolsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    debugToolsViewModel: DebugToolsViewModel = hiltViewModel()
) {
    val uiState by debugToolsViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(null) {
        debugToolsViewModel.refreshAll()
    }

    val selectedLanguageTag = uiState.selectedLanguage.uppercase()
    val metrics = listOf(
        MetricCardData(
            stringResource(ResourceR.string.core_resources_debug_metric_words_lang, "ES"),
            uiState.totalWordsEs.toString()
        ),
        MetricCardData(
            stringResource(ResourceR.string.core_resources_debug_metric_words_lang, "EN"),
            uiState.totalWordsEn.toString()
        ),
        MetricCardData(
            stringResource(ResourceR.string.core_resources_debug_metric_words_lang, selectedLanguageTag),
            uiState.selectedLanguageWords.toString()
        ),
        MetricCardData(
            stringResource(ResourceR.string.core_resources_debug_metric_categories_lang, "ES"),
            uiState.categoriesEs.toString()
        ),
        MetricCardData(
            stringResource(ResourceR.string.core_resources_debug_metric_categories_lang, "EN"),
            uiState.categoriesEn.toString()
        ),
        MetricCardData(
            stringResource(ResourceR.string.core_resources_debug_metric_coverage_lang, "ES"),
            "${uiState.categoryCoverageEs.roundToInt()}%"
        ),
        MetricCardData(
            stringResource(ResourceR.string.core_resources_debug_metric_coverage_lang, "EN"),
            "${uiState.categoryCoverageEn.roundToInt()}%"
        ),
        MetricCardData(
            stringResource(ResourceR.string.core_resources_debug_metric_coverage_lang, selectedLanguageTag),
            "${uiState.categoryCoverageSelectedLanguage.roundToInt()}%"
        ),
        MetricCardData(
            stringResource(ResourceR.string.core_resources_debug_metric_avg_clues_lang, selectedLanguageTag),
            uiState.averageCluesInSelectedLanguage.roundToInt().toString()
        )
    ).toImmutableList()

    DebugToolsDesign(
        modifier = modifier,
        data = DebugToolsViewData(
            metrics = metrics,
            selectedLanguage = uiState.selectedLanguage,
            latestWord = uiState.latestWordInSelectedLanguage,
            topCategory = uiState.topCategoryInSelectedLanguage,
            isLoading = uiState.isLoading,
            errorMessage = uiState.errorMessage
        ),
        actions = DebugToolsActions(
            onBack = onBack,
            onRefresh = debugToolsViewModel::refreshAll,
            onImportDefaultWords = debugToolsViewModel::importDefaultWords
        )
    )
}




