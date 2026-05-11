package es.sebas1705.debug.models

internal data class DebugToolsActions(
    val onBack: () -> Unit,
    val onRefresh: () -> Unit,
    val onImportDefaultWords: () -> Unit
)

