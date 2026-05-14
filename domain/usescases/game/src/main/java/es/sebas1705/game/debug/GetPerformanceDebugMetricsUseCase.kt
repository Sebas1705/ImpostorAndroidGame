package es.sebas1705.game.debug

import android.os.SystemClock
import es.sebas1705.models.DebugPerformanceMetricsModel
import javax.inject.Inject

class GetPerformanceDebugMetricsUseCase @Inject constructor() {
    operator fun invoke(
        refreshDurationMs: Long,
        resetDurationMs: Long
    ): DebugPerformanceMetricsModel {
        val runtime = Runtime.getRuntime()
        val usedHeap = (runtime.totalMemory() - runtime.freeMemory()) / BYTES_IN_MB
        val maxHeap = runtime.maxMemory() / BYTES_IN_MB

        return DebugPerformanceMetricsModel(
            refreshDurationMs = refreshDurationMs,
            resetDurationMs = resetDurationMs,
            appUptimeMs = SystemClock.elapsedRealtime(),
            usedHeapMb = usedHeap,
            maxHeapMb = maxHeap
        )
    }

    private companion object {
        const val BYTES_IN_MB = 1024L * 1024L
    }
}

