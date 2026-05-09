package es.sebas1705.repositories.repos

import android.os.Bundle
import es.sebas1705.analytics.config.EventLog
import es.sebas1705.analytics.config.UserProperty
import es.sebas1705.analytics.datasources.LogEventDataSource
import es.sebas1705.analytics.datasources.UserPropertiesDataSource
import es.sebas1705.analytics.model.AnalyticsModel
import es.sebas1705.common.managers.ClassLogData
import es.sebas1705.repositories.interfaces.IAnalyticsRepository
import javax.inject.Inject

/**
 * Analytics repository implementation
 *
 * @property logEventDataSource [LogEventDataSource]: Data source for logging events
 * @property userPropertiesDataSource [UserPropertiesDataSource]: Data source for user properties
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class AnalyticsRepository @Inject constructor(
    private val logEventDataSource: LogEventDataSource,
    private val userPropertiesDataSource: UserPropertiesDataSource
) : IAnalyticsRepository {

    override fun logEvent(event: EventLog, bundle: Bundle) =
        logEventDataSource.logEvent(event, bundle)

    override fun logError(classLogData: ClassLogData, error: String) =
        logEventDataSource.logError(classLogData, error)

    override fun setUserProperty(property: UserProperty, value: String) =
        userPropertiesDataSource.setUserProperty(property, value)

    override fun sendEvent(analyticsModel: AnalyticsModel) =
        logEventDataSource.sendEvent(analyticsModel)
}