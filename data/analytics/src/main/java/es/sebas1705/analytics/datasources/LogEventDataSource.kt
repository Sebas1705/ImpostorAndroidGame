package es.sebas1705.analytics.datasources

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import es.sebas1705.analytics.config.EventLog
import es.sebas1705.analytics.model.AnalyticsModel
import es.sebas1705.common.managers.ClassLogData
import es.sebas1705.common.utlis.extensions.types.logE
import es.sebas1705.common.utlis.extensions.types.logI
import javax.inject.Inject

/**
 * Data source for logging events to Firebase Analytics.
 *
 * @property firebaseAnalytics [FirebaseAnalytics]: Instance of Firebase Analytics.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class LogEventDataSource @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) {

    /**
     * Log an event with the given [EventLog] and [Bundle].
     *
     * @param event [EventLog]: The event to log.
     * @param bundle [Bundle]: The bundle containing event data.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun logEvent(event: EventLog, bundle: Bundle) {
        logI("analytics.event send tag=${event.tag} bundleKeys=${bundle.keySet().size}")
        firebaseAnalytics.logEvent(event.tag, bundle)
    }

    /**
     * Log an error event with the given [ClassLogData] and error message.
     *
     * @param classLogData [ClassLogData]: The class that logs the event.
     * @param error [String]: The error message to log.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun logError(classLogData: ClassLogData, error: String) {
        logE("analytics.error send source=${classLogData.className} message=$error")
        firebaseAnalytics.logEvent(EventLog.Error.tag) {
            param("package", classLogData.packageName)
            param("class", classLogData.className)
            param("error", error)
        }
    }

    /**
     * Send a configured event model to Firebase Analytics.
     *
     * @param analyticsModel [AnalyticsModel]: The model containing the event data.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun sendEvent(analyticsModel: AnalyticsModel) {
        logI(
            "analytics.eventModel send title=${analyticsModel.title} " +
                "strings=${analyticsModel.analyticsString.size} " +
                "longs=${analyticsModel.analyticsLong.size} " +
                "doubles=${analyticsModel.analyticsDouble.size} " +
                "bundles=${analyticsModel.analyticsBundle.size} " +
                "bundleArrays=${analyticsModel.analyticsBundleArray.size}"
        )
        firebaseAnalytics.logEvent(analyticsModel.title) {
            analyticsModel.analyticsString.forEach { (key, value) -> param(key, value) }
            analyticsModel.analyticsLong.forEach { (key, value) -> param(key, value) }
            analyticsModel.analyticsDouble.forEach { (key, value) -> param(key, value) }
            analyticsModel.analyticsBundle.forEach { (key, value) -> param(key, value) }
            analyticsModel.analyticsBundleArray.forEach { (key, value) -> param(key, value) }
        }
    }
}