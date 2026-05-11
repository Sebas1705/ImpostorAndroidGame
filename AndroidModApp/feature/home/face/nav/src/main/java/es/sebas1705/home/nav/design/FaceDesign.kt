package es.sebas1705.home.nav.design

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudQueue
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material.icons.outlined.OfflineBolt
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import es.sebas1705.common.utlis.UiModePreviews
import es.sebas1705.core.resources.R
import es.sebas1705.home.nav.components.GameCoverCard
import es.sebas1705.home.nav.components.ModeCard
import es.sebas1705.home.nav.viewmodel.FaceState
import es.sebas1705.ui.theme.AppTheme
import es.sebas1705.ui.theme.makeTitle

@Composable
@Suppress("LongMethod")
fun FaceDesign(
    modifier: Modifier = Modifier,
    faceState: FaceState = FaceState(),
    onOpenUser: () -> Unit = {},
    onOpenCategories: () -> Unit = {},
    onOpenMode: () -> Unit = {},
    onStartOfflineGame: () -> Unit = {},
    onOpenSettings: () -> Unit = {}
) {
    val selectedOffline = rememberSaveable { mutableStateOf(true) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onOpenSettings,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = stringResource(R.string.core_resources_settings_title),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(contentType = "contentType1") {
                GameCoverCard()
            }
            item(contentType = "contentType2") {
                Text(
                    text = stringResource(R.string.core_resources_face_environment),
                    style = MaterialTheme.typography.titleLarge.makeTitle(),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            item(contentType = "contentType3") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ModeCard(
                        title = stringResource(R.string.core_resources_face_offline_mode),
                        description = stringResource(R.string.core_resources_face_offline_mode_desc),
                        icon = Icons.Outlined.OfflineBolt,
                        onClick = {
                            selectedOffline.value = true
                        },
                        modifier = Modifier.weight(1f),
                        selected = selectedOffline.value
                    )
                    ModeCard(
                        title = stringResource(R.string.core_resources_face_online_mode),
                        description = stringResource(R.string.core_resources_face_online_mode_desc),
                        icon = Icons.Outlined.CloudQueue,
                        onClick = {
                            selectedOffline.value = false
                        },
                        modifier = Modifier.weight(1f),
                        selected = !selectedOffline.value
                    )
                }
            }

            if (selectedOffline.value) {
                item(contentType = "contentType4") {
                    Card(
                        modifier = Modifier.padding(bottom = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.LocalLibrary,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = stringResource(R.string.core_resources_face_categories),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(
                                    text = stringResource(
                                        R.string.core_resources_face_categories_selected,
                                        faceState.categoriesStates.count { it.value }
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Button(
                                    onClick = onOpenCategories,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(stringResource(R.string.core_resources_face_open_categories))
                                }
                            }
                        }
                    }
                }

                item(contentType = "contentType5") {
                    Card(
                        modifier = Modifier.padding(bottom = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.People,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = stringResource(R.string.core_resources_face_users),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(
                                    text = stringResource(
                                        R.string.core_resources_face_users_count,
                                        faceState.users.size
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Button(onClick = onOpenUser, modifier = Modifier.fillMaxWidth()) {
                                    Text(stringResource(R.string.core_resources_face_open_users))
                                }
                            }
                        }
                    }
                }

                item(contentType = "contentType8") {
                    Card(
                        modifier = Modifier.padding(bottom = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = stringResource(R.string.core_resources_face_mode),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(
                                    text = stringResource(
                                        R.string.core_resources_face_mode_summary,
                                        when (faceState.mode) {
                                            es.sebas1705.models.Modes.Classic -> stringResource(
                                                R.string.core_resources_game_mode_classic_name
                                            )
                                            es.sebas1705.models.Modes.Chaos -> stringResource(
                                                R.string.core_resources_game_mode_chaos_name
                                            )
                                        },
                                        faceState.impostors
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(
                                    text = stringResource(
                                        R.string.core_resources_game_mode_result_impostors_summary,
                                        if (faceState.showImpostorsInResult) {
                                            stringResource(R.string.core_resources_game_mode_result_impostors_visible)
                                        } else {
                                            stringResource(R.string.core_resources_game_mode_result_impostors_hidden)
                                        }
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Button(onClick = onOpenMode, modifier = Modifier.fillMaxWidth()) {
                                    Text(stringResource(R.string.core_resources_game_mode_open))
                                }
                            }
                        }
                    }
                }

                item(contentType = "contentType9") {
                    Button(
                        onClick = onStartOfflineGame,
                        enabled = faceState.users.size >= 3 &&
                            faceState.categoriesStates.any { it.value },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Text(stringResource(R.string.core_resources_face_start_offline_game))
                    }
                }
            } else {
                item(contentType = "contentType6") {
                    Text(
                        text = stringResource(R.string.core_resources_face_online_coming_soon),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            if (!faceState.errorMessage.isNullOrBlank()) {
                item(contentType = "contentType7") {
                    Text(
                        text = faceState.errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@UiModePreviews
@Composable
private fun Preview() {
    AppTheme {
        FaceDesign()
    }
}

