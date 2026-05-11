package es.sebas1705.main.models

import es.sebas1705.main.AppGraph

internal data class AppNavOverlayState(
    val currentDestination: AppGraph?,
    val showSettingsDialog: Boolean
)

