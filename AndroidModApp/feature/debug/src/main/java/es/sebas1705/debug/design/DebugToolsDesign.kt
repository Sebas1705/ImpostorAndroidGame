package es.sebas1705.debug.design

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.sebas1705.core.resources.R as ResourceR
import es.sebas1705.debug.components.DebugActionsCard
import es.sebas1705.debug.components.DebugInfoCard
import es.sebas1705.debug.components.MetricCard
import es.sebas1705.debug.models.DebugToolsActions
import es.sebas1705.debug.models.DebugToolsViewData

private fun LazyListScope.metricsSection(
    data: DebugToolsViewData
) {
    data.metrics.chunked(2).forEach { rowMetrics ->
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                rowMetrics.forEach {
                    MetricCard(data = it, modifier = Modifier.weight(1f))
                }
                if (rowMetrics.size == 1) {
                    Text("", modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

private fun LazyListScope.debugDataSection(data: DebugToolsViewData) {
    item {
        DebugInfoCard(
            title = stringResource(ResourceR.string.core_resources_debug_selected_app_language),
            value = data.selectedLanguage.uppercase()
        )
    }
    item {
        DebugInfoCard(
            title = stringResource(ResourceR.string.core_resources_debug_latest_word_selected_language),
            value = data.latestWord ?: stringResource(ResourceR.string.core_resources_debug_no_data),
            subtitle = stringResource(
                ResourceR.string.core_resources_debug_top_category,
                data.topCategory ?: stringResource(ResourceR.string.core_resources_debug_unknown)
            )
        )
    }
    if (data.isLoading) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
    if (!data.errorMessage.isNullOrBlank()) {
        item {
            Text(
                text = data.errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun DebugToolsDesign(
    modifier: Modifier = Modifier,
    data: DebugToolsViewData,
    actions: DebugToolsActions
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(ResourceR.string.core_resources_debug_title)) },
                navigationIcon = {
                    IconButton(onClick = actions.onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(ResourceR.string.core_resources_back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = actions.onRefresh) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = stringResource(ResourceR.string.core_resources_refresh)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            metricsSection(data)
            item {
                DebugActionsCard(
                    onImportDefaultWords = actions.onImportDefaultWords,
                    onRefresh = actions.onRefresh
                )
            }
            debugDataSection(data)
        }
    }
}


