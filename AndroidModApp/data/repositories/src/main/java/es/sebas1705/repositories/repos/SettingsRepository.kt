package es.sebas1705.repositories.repos

import es.sebas1705.datastore.datasources.SettingsPreferencesDataSource
import es.sebas1705.datastore.model.SettingsData
import es.sebas1705.repositories.interfaces.ISettingsRepository
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
class SettingsRepository @Inject constructor(
    private val settingsPreferencesDataSource: SettingsPreferencesDataSource
): ISettingsRepository {

    override fun read(): Flow<SettingsData> =
        settingsPreferencesDataSource.getSettingsData()

    override suspend fun update(
        settingsData: SettingsData
    ) {
        settingsPreferencesDataSource.saveFirstTime(settingsData.firstTime)
        settingsPreferencesDataSource.saveMusicVolume(settingsData.musicVolume)
        settingsPreferencesDataSource.saveSoundVolume(settingsData.soundVolume)
        settingsPreferencesDataSource.saveAppContrast(settingsData.appContrast)
    }

}