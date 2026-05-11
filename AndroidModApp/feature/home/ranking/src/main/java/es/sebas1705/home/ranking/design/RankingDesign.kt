package es.sebas1705.home.ranking.design

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.sebas1705.common.utlis.UiModePreviews
import es.sebas1705.core.resources.R as ResourceR
import es.sebas1705.home.ranking.components.RankingBannerCard
import es.sebas1705.home.ranking.models.OfflineRankingRowUi
import es.sebas1705.home.ranking.viewmodel.RankingTab
import es.sebas1705.ui.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
@Suppress("LongMethod")
fun RankingDesign(
    modifier: Modifier = Modifier,
    selectedTab: RankingTab = RankingTab.Offline,
    offlineRows: ImmutableList<OfflineRankingRowUi> = persistentListOf(),
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onSelectTab: (RankingTab) -> Unit = {}
) {
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
                PrimaryTabRow(selectedTabIndex = tabs.indexOfFirst { it.first == selectedTab }) {
                    tabs.forEach { (tab, label) ->
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { onSelectTab(tab) },
                            text = { Text(label) }
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
                            RankingTableHeader()
                        }
                        items(
                            items = offlineRows,
                            key = { row -> row.playerName },
                            contentType = { _ -> "contentType9" }
                        ) { row ->
                            RankingTableRow(row)
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
private fun RankingTableHeader() {
    RankingTableCells(
        position = stringResource(ResourceR.string.core_resources_ranking_col_position),
        player = stringResource(ResourceR.string.core_resources_ranking_col_player),
        civilianWins = stringResource(ResourceR.string.core_resources_ranking_col_civilian_wins),
        impostorWins = stringResource(ResourceR.string.core_resources_ranking_col_impostor_wins),
        totalWins = stringResource(ResourceR.string.core_resources_ranking_col_total),
        textStyle = MaterialTheme.typography.titleSmall
    )
}

@Composable
private fun RankingTableRow(row: OfflineRankingRowUi) {
    RankingTableCells(
        position = row.position.toString(),
        player = row.playerName,
        civilianWins = row.civilianWins.toString(),
        impostorWins = row.impostorWins.toString(),
        totalWins = row.totalWins.toString(),
        textStyle = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun RankingTableCells(
    position: String,
    player: String,
    civilianWins: String,
    impostorWins: String,
    totalWins: String,
    textStyle: androidx.compose.ui.text.TextStyle
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = position, style = textStyle, modifier = Modifier.weight(0.6f))
        Text(text = player, style = textStyle, modifier = Modifier.weight(2f))
        Text(text = civilianWins, style = textStyle, modifier = Modifier.weight(1f))
        Text(text = impostorWins, style = textStyle, modifier = Modifier.weight(1f))
        Text(text = totalWins, style = textStyle, modifier = Modifier.weight(0.9f))
    }
}

@UiModePreviews
@Composable
private fun Preview() {
    AppTheme {
        RankingDesign()
    }
}

