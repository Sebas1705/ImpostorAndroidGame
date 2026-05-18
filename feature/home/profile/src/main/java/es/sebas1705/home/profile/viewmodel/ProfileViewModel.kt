package es.sebas1705.home.profile.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.authentication.GetUserProfileDataUseCase
import es.sebas1705.authentication.SignOutUseCase
import es.sebas1705.common.mvi.MVIBaseViewModel
import es.sebas1705.core.resources.R
import es.sebas1705.game.settings.ReadGameUseCase
import es.sebas1705.home.profile.models.OfflineProfileRecordRowUi
import es.sebas1705.models.Categories
import es.sebas1705.models.GameModel
import es.sebas1705.models.OfflineRankingModel
import es.sebas1705.ranking.ReadOfflineRankingUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val readOfflineRankingUseCase: ReadOfflineRankingUseCase,
    private val readGameUseCase: ReadGameUseCase,
    private val getUserProfileDataUseCase: GetUserProfileDataUseCase,
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext context: Context
) : MVIBaseViewModel<ProfileState, ProfileIntent>(context) {

    override fun initState(): ProfileState = ProfileState(
        offlineRecordSort = readSavedProfileSort(savedStateHandle)
    )

    override fun onInit() {
        super.onInit()
        startLoading()
    }

    override fun intentHandler(intent: ProfileIntent) =
        when (intent) {
            ProfileIntent.Load -> loadProfileData()
            ProfileIntent.SignOut -> signOut()
            ProfileIntent.ConsumeSignOutNavigation -> consumeSignOutNavigation()
            is ProfileIntent.SelectTab -> updateUi { it.copy(selectedTab = intent.tab) }
            is ProfileIntent.ToggleOfflineRecordSort -> toggleOfflineSort(intent.column)
        }

    private fun loadProfileData() = execute(Dispatchers.IO) {
        startLoading()
        updateUi { it.copy(errorMessage = null) }

        // Load Google user profile immediately (synchronous — no network call).
        val userProfile = getUserProfileDataUseCase()
        updateUi {
            it.copy(
                userName = userProfile.displayName,
                userEmail = userProfile.email,
                userPhotoUrl = userProfile.photoUrl,
            )
        }

        runCatching {
            val game = readGameUseCase().first()
            val rankingEntries = readOfflineRankingUseCase()
            buildProfilePayload(game, rankingEntries)
        }
            .onSuccess { payload ->
                val currentSort = uiState.value.offlineRecordSort
                stopLoading()
                updateUi {
                    it.copy(
                        offlineRecordRows = sortProfileOfflineRows(payload.offlineRows, currentSort),
                        rolePreference = payload.rolePreference,
                        favoriteCategory = payload.favoriteCategory,
                        matchesPlayed = payload.matchesPlayed,
                        currentStreak = payload.currentStreak,
                        bestStreak = payload.bestStreak
                    )
                }
            }
            .onFailure { throwable ->
                stopLoading()
                updateUi { it.copy(errorMessage = throwable.message) }
            }
    }

    private fun buildProfilePayload(
        game: GameModel,
        rankingEntries: List<OfflineRankingModel>
    ): ProfileStatePayload {
        val primaryPlayer = game.players.firstOrNull().orEmpty().trim()
        val primaryPlayerEntry = rankingEntries.firstOrNull {
            it.playerName.equals(primaryPlayer, ignoreCase = true)
        }

        val roleSource = primaryPlayerEntry ?: aggregateTotalsEntry(rankingEntries)
        val rolePreference = if (roleSource.impostorWins > roleSource.civilianWins)
            ProfileRolePreference.ImpostorSpecialist
        else ProfileRolePreference.ImpostorHunter

        return ProfileStatePayload(
            offlineRows = rankingEntries.mapIndexed { index, entry ->
                OfflineProfileRecordRowUi(
                    position = index,
                    playerName = entry.playerName,
                    civilianWins = entry.civilianWins,
                    impostorWins = entry.impostorWins,
                    totalWins = entry.totalWins,
                    currentStreak = entry.currentStreak
                )
            }.toImmutableList(),
            rolePreference = rolePreference,
            favoriteCategory = game.selectedCategories.firstOrNull(),
            matchesPlayed = primaryPlayerEntry?.totalWins ?: rankingEntries.sumOf { it.totalWins },
            currentStreak = primaryPlayerEntry?.currentStreak
                ?: rankingEntries.maxOfOrNull { it.currentStreak }
                ?: 0,
            bestStreak = primaryPlayerEntry?.bestStreak
                ?: rankingEntries.maxOfOrNull { it.bestStreak }
                ?: 0
        )
    }

    private fun aggregateTotalsEntry(entries: List<OfflineRankingModel>): OfflineRankingModel =
        OfflineRankingModel(
            playerName = "all",
            civilianWins = entries.sumOf { it.civilianWins },
            impostorWins = entries.sumOf { it.impostorWins }
        )

    private data class ProfileStatePayload(
        val offlineRows: kotlinx.collections.immutable.ImmutableList<OfflineProfileRecordRowUi>,
        val rolePreference: ProfileRolePreference,
        val favoriteCategory: Categories?,
        val matchesPlayed: Int,
        val currentStreak: Int,
        val bestStreak: Int
    )

    private fun toggleOfflineSort(column: ProfileOfflineRecordSortColumn) = execute {
        updateUi { state ->
            val nextSort = resolveNextProfileSort(state.offlineRecordSort, column)
            persistProfileSort(savedStateHandle, nextSort)
            state.copy(
                offlineRecordSort = nextSort,
                offlineRecordRows = sortProfileOfflineRows(state.offlineRecordRows, nextSort)
            )
        }
    }

    private fun signOut() = execute(Dispatchers.IO) {
        val isSuccess = signOutUseCase()
        updateUi {
            if (isSuccess) {
                it.copy(navigateToLogin = true, errorMessage = null)
            } else {
                it.copy(errorMessage = context.getString(R.string.core_resources_settings_sign_out_error))
            }
        }
    }

    private fun consumeSignOutNavigation() = execute {
        updateUi { it.copy(navigateToLogin = false) }
    }
}

private const val PROFILE_SORT_COLUMN_KEY = "profile.sort.column"
private const val PROFILE_SORT_DIRECTION_KEY = "profile.sort.direction"

private fun readSavedProfileSort(savedStateHandle: SavedStateHandle): ProfileOfflineRecordSort {
    val savedColumn = savedStateHandle.get<String>(PROFILE_SORT_COLUMN_KEY)
    val savedDirection = savedStateHandle.get<String>(PROFILE_SORT_DIRECTION_KEY)

    val column = savedColumn?.let {
        runCatching { ProfileOfflineRecordSortColumn.valueOf(it) }.getOrNull()
    } ?: ProfileOfflineRecordSortColumn.TotalWins

    val direction = savedDirection?.let {
        runCatching { ProfileSortDirection.valueOf(it) }.getOrNull()
    } ?: ProfileSortDirection.Descending

    return ProfileOfflineRecordSort(column = column, direction = direction)
}

private fun persistProfileSort(
    savedStateHandle: SavedStateHandle,
    sort: ProfileOfflineRecordSort
) {
    savedStateHandle[PROFILE_SORT_COLUMN_KEY] = sort.column.name
    savedStateHandle[PROFILE_SORT_DIRECTION_KEY] = sort.direction.name
}

private fun resolveNextProfileSort(
    current: ProfileOfflineRecordSort,
    column: ProfileOfflineRecordSortColumn
): ProfileOfflineRecordSort {
    if (current.column != column) {
        return ProfileOfflineRecordSort(
            column = column,
            direction = if (column == ProfileOfflineRecordSortColumn.Player) {
                ProfileSortDirection.Ascending
            } else {
                ProfileSortDirection.Descending
            }
        )
    }
    val nextDirection = if (current.direction == ProfileSortDirection.Ascending) {
        ProfileSortDirection.Descending
    } else {
        ProfileSortDirection.Ascending
    }
    return current.copy(direction = nextDirection)
}

private fun sortProfileOfflineRows(
    rows: List<OfflineProfileRecordRowUi>,
    sort: ProfileOfflineRecordSort
) = rows
    .sortedWith(compareByDescending<OfflineProfileRecordRowUi> { sortableProfileValue(it, sort.column) }
        .thenBy { it.playerName.lowercase() })
    .let { sortedRows ->
        if (sort.direction == ProfileSortDirection.Descending) {
            sortedRows
        } else {
            sortedRows.reversed()
        }
    }
    .mapIndexed { index, row -> row.copy(position = index + 1) }
    .toImmutableList()

private fun sortableProfileValue(
    row: OfflineProfileRecordRowUi,
    column: ProfileOfflineRecordSortColumn
): Comparable<*> = when (column) {
    ProfileOfflineRecordSortColumn.Position -> row.position
    ProfileOfflineRecordSortColumn.Player -> row.playerName.lowercase()
    ProfileOfflineRecordSortColumn.CivilianWins -> row.civilianWins
    ProfileOfflineRecordSortColumn.ImpostorWins -> row.impostorWins
    ProfileOfflineRecordSortColumn.TotalWins -> row.totalWins
    ProfileOfflineRecordSortColumn.CurrentStreak -> row.currentStreak
}
