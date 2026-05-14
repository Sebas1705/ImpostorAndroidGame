package es.sebas1705.repositories.repos

import es.sebas1705.datastore.datasources.SettingsPreferencesDataSource
import es.sebas1705.datastore.model.SettingsData
import es.sebas1705.repositories.interfaces.IAppSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Settings repository implementation
 *
 * @property settingsPreferencesDataSource [SettingsPreferencesDataSource]: Data source for settings preferences
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class AppAppSettingsRepository @Inject constructor(
    private val settingsPreferencesDataSource: SettingsPreferencesDataSource
): IAppSettingsRepository {

    override fun read(): Flow<SettingsData> =
        settingsPreferencesDataSource.getSettingsData()

    override suspend fun update(
        settingsData: SettingsData
    ) {
        settingsPreferencesDataSource.saveSettings(settingsData)
    }
}