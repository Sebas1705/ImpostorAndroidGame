package es.sebas1705.game.debug

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.models.DebugDeviceMetricsModel
import java.util.Locale
import javax.inject.Inject

class GetDeviceDebugMetricsUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(): DebugDeviceMetricsModel {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)

        return DebugDeviceMetricsModel(
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            androidVersion = Build.VERSION.RELEASE,
            sdkInt = Build.VERSION.SDK_INT,
            totalRamMb = memoryInfo.totalMem / BYTES_IN_MB,
            availableRamMb = memoryInfo.availMem / BYTES_IN_MB,
            isLowRamDevice = activityManager.isLowRamDevice,
            localeTag = Locale.getDefault().toLanguageTag()
        )
    }

    private companion object {
        const val BYTES_IN_MB = 1024L * 1024L
    }
}

