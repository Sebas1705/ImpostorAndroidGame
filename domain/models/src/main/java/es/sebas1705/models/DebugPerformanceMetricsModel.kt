package es.sebas1705.models

data class DebugPerformanceMetricsModel(
    val refreshDurationMs: Long,
    val resetDurationMs: Long,
    val appUptimeMs: Long,
    val usedHeapMb: Long,
    val maxHeapMb: Long
)

