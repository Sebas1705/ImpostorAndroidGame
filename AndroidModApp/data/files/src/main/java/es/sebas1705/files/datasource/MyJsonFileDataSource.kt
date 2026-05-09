package es.sebas1705.files.datasource

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.analytics.datasources.LogEventDataSource
import es.sebas1705.common.managers.ClassLogData
import es.sebas1705.files.config.SettingsFL
import es.sebas1705.files.json.MyJson
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Class to represent the repository of the files
 *
 * @property context [Context]: Application context
 * @property logEventDataSource [LogEventDataSource]: Data source for logging events
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class MyJsonFileDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val logEventDataSource: LogEventDataSource
) : ClassLogData() {

    fun readJsonFile(): MyJson {
        try {
            val json =
                context.assets.open(SettingsFL.MY_JSON).bufferedReader().use {
                    it.readText()
                }

            return Json.decodeFromString<MyJson>(json)
        } catch (ex: Exception) {
            logEventDataSource.logError(this, ex.message.toString())
            return MyJson("", 0)
        }
    }

}