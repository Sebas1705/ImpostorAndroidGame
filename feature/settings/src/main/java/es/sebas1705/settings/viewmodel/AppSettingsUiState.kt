package es.sebas1705.settings.viewmodel

import es.sebas1705.common.mvi.MVIBaseState
import es.sebas1705.models.SettingsModel

data class AppSettingsUiState(
    val settings: SettingsModel? = null,
    val errorMessage: String? = null
) : MVIBaseState


