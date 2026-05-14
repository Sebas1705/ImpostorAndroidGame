package es.sebas1705.realtime.datasources

import com.google.firebase.database.FirebaseDatabase
import es.sebas1705.analytics.datasources.LogEventDataSource
import es.sebas1705.common.managers.ClassLogData
import es.sebas1705.common.managers.TaskFlowManager
import es.sebas1705.realtime.config.SettingsRT
import javax.inject.Inject

/**
 * Data source for managing JSON data in Firebase Realtime Database.
 *
 * @property database [FirebaseDatabase]: Firebase database
 * @property logEventDataSource [LogEventDataSource]: Data source for logging events
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class MyJsonRealtimeDataSource @Inject constructor(
    private val database: FirebaseDatabase,
    private val logEventDataSource: LogEventDataSource
) : ClassLogData() {

    //Managers:
    private val taskFlowManager = TaskFlowManager(
        this,
        logEventDataSource::logError,
        SettingsRT.ERROR_GENERIC_MESSAGE_FAIL,
        SettingsRT.ERROR_GENERIC_MESSAGE_EX
    )

    //References:

    //Listeners:

    //Tasks:

    //Sets Listeners:

    //Removes Listeners:

}