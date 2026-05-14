package es.sebas1705.repositories.interfaces

import android.os.Bundle
import es.sebas1705.analytics.config.EventLog
import es.sebas1705.analytics.config.UserProperty
import es.sebas1705.analytics.model.AnalyticsModel
import es.sebas1705.common.managers.ClassLogData

/**
 * Repository interface to log events and set user properties
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
interface IAnalyticsRepository {

    /**
     * Log an event
     *
     * @param event [EventLog]: Event to log
     * @param bundle [Bundle]: Bundle with the event data
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun logEvent(event: EventLog, bundle: Bundle)

    /**
     * Log an error event
     *
     * @param classLogData [ClassLogData]: Class that logs the event
     * @param error [String]: Error message
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun logError(classLogData: ClassLogData, error: String)

    /**
     * Set a user property
     *
     * @param property [UserProperty]: Property to set
     * @param value [String]: Value to set
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun setUserProperty(property: UserProperty, value: String)

    /**
     * Send an event
     *
     * @param analyticsModel [AnalyticsModel]: Model with the event data
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun sendEvent(analyticsModel: AnalyticsModel)

}