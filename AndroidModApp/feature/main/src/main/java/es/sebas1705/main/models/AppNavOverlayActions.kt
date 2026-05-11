package es.sebas1705.main.models

internal data class AppNavOverlayActions(
    val onOpenSettings: () -> Unit,
    val onDismissSettings: () -> Unit,
    val onOpenCategories: () -> Unit,
    val onOpenDebugTools: () -> Unit,
    val onSignedOut: () -> Unit
)

