package es.sebas1705.home.ranking.design

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import es.sebas1705.common.utlis.UiModePreviews
import es.sebas1705.home.ranking.components.RankingBannerCard
import es.sebas1705.home.ranking.models.OfflineRankingRowUi
import es.sebas1705.home.ranking.viewmodel.RankingOfflineSort
import es.sebas1705.home.ranking.viewmodel.RankingOfflineSortColumn
import es.sebas1705.home.ranking.viewmodel.RankingSortDirection
import es.sebas1705.home.ranking.viewmodel.RankingTab
import es.sebas1705.ui.adaptive.LocalForceCompactTables
import es.sebas1705.ui.adaptive.TableSortAnimation
import es.sebas1705.ui.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import es.sebas1705.core.resources.R as ResourceR

@Composable
@Suppress("LongMethod")
fun RankingDesign(
    modifier: Modifier = Modifier,
    selectedTab: RankingTab = RankingTab.Offline,
    offlineRows: ImmutableList<OfflineRankingRowUi> = persistentListOf(),
    offlineSort: RankingOfflineSort = RankingOfflineSort(),
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onSelectTab: (RankingTab) -> Unit = {},
    onToggleOfflineSort: (RankingOfflineSortColumn) -> Unit = {}
) {
    val forceCompactTables = LocalForceCompactTables.current
    val isCompactPhone = forceCompactTables || LocalConfiguration.current.screenWidthDp < 420
    val isVeryCompactPhone = forceCompactTables || LocalConfiguration.current.screenWidthDp < 360
    val tableScrollState = rememberScrollState()
    val tabEdgePadding = if (isCompactPhone) 4.dp else 12.dp

    val title = stringResource(ResourceR.string.core_resources_ranking_title)
    val subtitle = stringResource(ResourceR.string.core_resources_ranking_subtitle)
    val tabs = listOf(
        RankingTab.Offline to stringResource(ResourceR.string.core_resources_ranking_tab_offline),
        RankingTab.Online to stringResource(ResourceR.string.core_resources_ranking_tab_online)
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(contentType = "contentType1") {
                RankingBannerCard()
            }
            item(contentType = "contentType2") {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            item(contentType = "contentType3") {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item(contentType = "contentType4") {
                PrimaryScrollableTabRow(
                    selectedTabIndex = tabs.indexOfFirst { it.first == selectedTab },
                    edgePadding = tabEdgePadding
                ) {
                    tabs.forEach { (tab, label) ->
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { onSelectTab(tab) },
                            text = {
                                Text(
                                    text = label,
                                    style = if (isVeryCompactPhone) {
                                        MaterialTheme.typography.labelMedium
                                    } else {
                                        MaterialTheme.typography.titleSmall
                                    },
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        )
                    }
                }
            }

            when (selectedTab) {
                RankingTab.Online -> {
                    item(contentType = "contentType5") {
                        Text(
                            text = stringResource(ResourceR.string.core_resources_ranking_online_not_implemented),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                RankingTab.Offline -> {
                    if (isLoading) {
                        item(contentType = "contentType6") {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    } else if (offlineRows.isEmpty()) {
                        item(contentType = "contentType7") {
                            Text(
                                text = stringResource(ResourceR.string.core_resources_ranking_offline_empty),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    } else {
                        item(contentType = "contentType8") {
                            RankingTableHeader(
                                scrollState = tableScrollState,
                                currentSort = offlineSort,
                                onSortBy = onToggleOfflineSort
                            )
                        }
                        items(
                            items = offlineRows,
                            key = { row -> row.playerName },
                            contentType = { _ -> "contentType9" }
                        ) { row ->
                            RankingTableRow(
                                row = row,
                                scrollState = tableScrollState,
                                textStyle = if (isVeryCompactPhone) {
                                    MaterialTheme.typography.bodySmall
                                } else {
                                    MaterialTheme.typography.bodyMedium
                                }
                            )
                        }
                    }
                }
            }

            if (!errorMessage.isNullOrBlank()) {
                item(contentType = "contentType10") {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun RankingTableHeader(
    scrollState: androidx.compose.foundation.ScrollState,
    currentSort: RankingOfflineSort,
    onSortBy: (RankingOfflineSortColumn) -> Unit
) {
    RankingTableCells(
        position = stringResource(ResourceR.string.core_resources_ranking_col_position),
        player = stringResource(ResourceR.string.core_resources_ranking_col_player),
        civilianWins = stringResource(ResourceR.string.core_resources_ranking_col_civilian_wins),
        impostorWins = stringResource(ResourceR.string.core_resources_ranking_col_impostor_wins),
        totalWins = stringResource(ResourceR.string.core_resources_ranking_col_total),
        textStyle = MaterialTheme.typography.titleSmall,
        scrollState = scrollState,
        isHeader = true,
        currentSort = currentSort,
        onSortBy = onSortBy
    )
}

@Composable
private fun RankingTableRow(row: OfflineRankingRowUi, scrollState: androidx.compose.foundation.ScrollState, textStyle: androidx.compose.ui.text.TextStyle) {
    RankingTableCells(
        position = row.position.toString(),
        player = row.playerName,
        civilianWins = row.civilianWins.toString(),
        impostorWins = row.impostorWins.toString(),
        totalWins = row.totalWins.toString(),
        textStyle = textStyle,
        scrollState = scrollState
    )
}

@Composable
@Suppress("LongParameterList", "LongMethod")
private fun RankingTableCells(
    position: String,
    player: String,
    civilianWins: String,
    impostorWins: String,
    totalWins: String,
    textStyle: androidx.compose.ui.text.TextStyle,
    scrollState: androidx.compose.foundation.ScrollState,
    isHeader: Boolean = false,
    currentSort: RankingOfflineSort = RankingOfflineSort(),
    onSortBy: (RankingOfflineSortColumn) -> Unit = {}
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .then(
                if (isHeader) {
                    Modifier.background(MaterialTheme.colorScheme.background)
                } else {
                    Modifier
                }
            )
            .horizontalScroll(scrollState)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        RankingTableCell(
            text = position,
            style = textStyle,
            modifier = Modifier.width(44.dp),
            isHeader = isHeader,
            sortDirection = currentSort.direction.takeIf {
                isHeader && currentSort.column == RankingOfflineSortColumn.Position
            },
            onClick = { onSortBy(RankingOfflineSortColumn.Position) }
        )
        RankingTableCell(
            text = player,
            style = textStyle,
            modifier = Modifier.width(140.dp),
            isHeader = isHeader,
            sortDirection = currentSort.direction.takeIf {
                isHeader && currentSort.column == RankingOfflineSortColumn.Player
            },
            onClick = { onSortBy(RankingOfflineSortColumn.Player) }
        )
        RankingTableCell(
            text = civilianWins,
            style = textStyle,
            modifier = Modifier.width(92.dp),
            isHeader = isHeader,
            sortDirection = currentSort.direction.takeIf {
                isHeader && currentSort.column == RankingOfflineSortColumn.CivilianWins
            },
            onClick = { onSortBy(RankingOfflineSortColumn.CivilianWins) }
        )
        RankingTableCell(
            text = impostorWins,
            style = textStyle,
            modifier = Modifier.width(92.dp),
            isHeader = isHeader,
            sortDirection = currentSort.direction.takeIf {
                isHeader && currentSort.column == RankingOfflineSortColumn.ImpostorWins
            },
            onClick = { onSortBy(RankingOfflineSortColumn.ImpostorWins) }
        )
        RankingTableCell(
            text = totalWins,
            style = textStyle,
            modifier = Modifier.width(92.dp),
            isHeader = isHeader,
            sortDirection = currentSort.direction.takeIf {
                isHeader && currentSort.column == RankingOfflineSortColumn.TotalWins
            },
            onClick = { onSortBy(RankingOfflineSortColumn.TotalWins) }
        )
    }
}

@Composable
@Suppress("LongMethod")
private fun RankingTableCell(
    text: String,
    style: androidx.compose.ui.text.TextStyle,
    modifier: Modifier,
    isHeader: Boolean,
    sortDirection: RankingSortDirection?,
    onClick: () -> Unit
) {
    if (!isHeader) {
        Text(
            text = text,
            style = style,
            modifier = modifier,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Normal
        )
        return
    }

    androidx.compose.foundation.layout.Row(
        modifier = modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        val animatedHeaderColor = animateColorAsState(
            targetValue = if (sortDirection != null) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            animationSpec = tween(durationMillis = TableSortAnimation.HeaderColorDurationMs),
            label = "rankingHeaderSortColor"
        )
        Text(
            text = text,
            style = style,
            color = animatedHeaderColor.value,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = if (sortDirection != null) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            }
        )
        AnimatedVisibility(
            visible = sortDirection != null,
            enter = fadeIn(animationSpec = tween(TableSortAnimation.IconEnterDurationMs)) +
                scaleIn(
                    initialScale = TableSortAnimation.IconScaleCollapsed,
                    animationSpec = tween(TableSortAnimation.IconEnterDurationMs)
                ),
            exit = fadeOut(animationSpec = tween(TableSortAnimation.IconExitDurationMs)) +
                scaleOut(
                    targetScale = TableSortAnimation.IconScaleCollapsed,
                    animationSpec = tween(TableSortAnimation.IconExitDurationMs)
                )
        ) {
            Icon(
                imageVector = if (sortDirection == RankingSortDirection.Ascending) {
                    Icons.Outlined.ArrowUpward
                } else {
                    Icons.Outlined.ArrowDownward
                },
                contentDescription = null,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

@UiModePreviews
@Composable
private fun Preview() {
    AppTheme {
        RankingDesign()
    }
}

