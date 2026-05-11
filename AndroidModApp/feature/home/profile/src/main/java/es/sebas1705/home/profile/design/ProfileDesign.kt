package es.sebas1705.home.profile.design

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.sebas1705.common.utlis.UiModePreviews
import es.sebas1705.core.resources.R as ResourceR
import es.sebas1705.home.profile.components.ProfileBannerCard
import es.sebas1705.home.profile.components.ProfileRowCard
import es.sebas1705.home.profile.models.ProfileRowData
import es.sebas1705.ui.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
@Suppress("LongMethod")
fun ProfileDesign(
    modifier: Modifier = Modifier,
    rows: ImmutableList<String> = persistentListOf(),
    errorMessage: String? = null,
    onSignOut: () -> Unit = {},
    onDebugNav: () -> Unit = {}
) {
    val rowItems = remember(rows) { rows.map(::ProfileRowData).toImmutableList() }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(contentType = "contentType1") {
                ProfileBannerCard()
            }
            item(contentType = "contentType2") {
                Text(
                    text = stringResource(ResourceR.string.core_resources_profile_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            item(contentType = "contentType3") {
                Text(
                    text = stringResource(ResourceR.string.core_resources_profile_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            items(
                items = rowItems,
                key = { row -> row.value },
                contentType = { _ -> "contentType4" }) { row ->
                ProfileRowCard(data = row)
            }
            item(contentType = "contentType5") {
                Button(onClick = onSignOut, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(ResourceR.string.core_resources_settings_sign_out))
                }
            }
            if (!errorMessage.isNullOrBlank()) {
                item(contentType = "contentType6") {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            item(contentType = "contentType7") {
                OutlinedButton(
                    onClick = onDebugNav,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(ResourceR.string.core_resources_settings_open_debug_tools))
                }
            }
        }
    }
}

@UiModePreviews
@Composable
private fun Preview() {
    AppTheme {
        ProfileDesign()
    }
}

