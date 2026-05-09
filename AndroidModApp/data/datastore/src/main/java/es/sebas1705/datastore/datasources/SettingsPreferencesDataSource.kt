package es.sebas1705.datastore.datasources

import androidx.datastore.core.DataStore
import es.sebas1705.datastore.SettingsPreferences
import es.sebas1705.datastore.config.DefaultValuesDS
import es.sebas1705.datastore.model.SettingsData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsPreferencesDataSource @Inject constructor(
    private val settingsPreferences: DataStore<SettingsPreferences>
) {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            settingsPreferences.updateData {
                it.toBuilder()
                    .setContrast(DefaultValuesDS.APP_UI_CONTRAST)
                    .setFirstTime(DefaultValuesDS.FIRST_TIME)
                    .setMusicVolume(DefaultValuesDS.MUSIC_VOLUME)
                    .setSoundVolume(DefaultValuesDS.SOUND_VOLUME)
                    .setDefaultSet(true)
                    .build()
            }
        }
    }

    /**
     * Settings data
     */

    private val settingsData = settingsPreferences.data.map {
        SettingsData(
            it.firstTime,
            it.musicVolume,
            it.soundVolume,
            it.contrast,
            it.defaultSet
        )
    }

    /**
     * Saves
     */

    /**
     * Update first time value
     *
     * @param firstTime [Boolean]: First time
     *
     * @return [SettingsData]: Updated settings data
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    suspend fun saveFirstTime(
        firstTime: Boolean
    ) = settingsPreferences.updateData {
        it.toBuilder().setFirstTime(firstTime).build()
    }

    /**
     * Update music volume
     *
     * @param musicVolume [Float]: Music volume
     *
     * @return [SettingsData]: Updated settings data
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    suspend fun saveMusicVolume(
        musicVolume: Float
    ) = settingsPreferences.updateData {
        it.toBuilder().setMusicVolume(musicVolume).build()
    }

    /**
     * Update sound volume
     *
     * @param soundVolume [Float]: Sound volume
     *
     * @return [SettingsData]: Updated settings data
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    suspend fun saveSoundVolume(
        soundVolume: Float
    ) = settingsPreferences.updateData {
        it.toBuilder().setSoundVolume(soundVolume).build()
    }

    /**
     * Update app contrast
     *
     * @param contrast [Int]: App contrast
     *
     * @return [SettingsData]: Updated settings data
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    suspend fun saveAppContrast(
        contrast: Int
    ) = settingsPreferences.updateData {
        it.toBuilder().setContrast(contrast).build()
    }

    /**
     * Read
     */

    /**
     * Get settings data
     *
     * @return [SettingsData]: Settings data flow
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun getSettingsData() = settingsData

}