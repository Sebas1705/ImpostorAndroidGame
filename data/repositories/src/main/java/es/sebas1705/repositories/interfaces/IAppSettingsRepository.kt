package es.sebas1705.repositories.interfaces

import es.sebas1705.datastore.model.SettingsData
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing settings
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
interface IAppSettingsRepository {

    /**
     * Reads the current settings
     *
     * @return [Flow]<[SettingsData]> Flow that emits the current settings data
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    fun read(): Flow<SettingsData>


    /**
     * Updates the settings with the provided data
     *
     * @param settingsData [SettingsData] The new settings data to update
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    suspend fun update(settingsData: SettingsData)
}