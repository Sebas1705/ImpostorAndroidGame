package es.sebas1705.mappers

import es.sebas1705.common.theme.ThemeContrast
import es.sebas1705.datastore.model.SettingsData
import es.sebas1705.models.SettingsModel

/**
 * Mapper to convert between [SettingsData] and [SettingsModel].
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
fun SettingsData.toModel() = SettingsModel(
    firstTime = firstTime,
    musicVolume = musicVolume,
    soundVolume = soundVolume,
    appContrast = ThemeContrast.entries[appContrast],
)

/**
 * Mapper to convert between [SettingsModel] and [SettingsData].
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
fun SettingsModel.toData() = SettingsData(
    firstTime = firstTime,
    musicVolume = musicVolume,
    soundVolume = soundVolume,
    appContrast = appContrast.ordinal,
    defaultSet = true
)