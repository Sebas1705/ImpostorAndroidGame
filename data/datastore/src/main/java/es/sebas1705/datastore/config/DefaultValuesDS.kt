package es.sebas1705.datastore.config

import es.sebas1705.common.theme.ThemeContrast
import es.sebas1705.datastore.config.DefaultValuesDS.APP_UI_CONTRAST
import es.sebas1705.datastore.config.DefaultValuesDS.FIRST_TIME
import es.sebas1705.datastore.config.DefaultValuesDS.MUSIC_VOLUME
import es.sebas1705.datastore.config.DefaultValuesDS.SOUND_VOLUME

/**
 * Default values for the data store
 *
 * @property FIRST_TIME [Boolean]: First time
 * @property MUSIC_VOLUME [Float]: App music volume
 * @property SOUND_VOLUME [Float]: App sound volume
 * @property APP_UI_CONTRAST [ThemeContrast]: App contrast
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
object DefaultValuesDS {
    const val FIRST_TIME = false
    const val MUSIC_VOLUME = 1.0f
    const val SOUND_VOLUME = 1.0f
    const val APP_UI_CONTRAST = 0
    const val APP_LANGUAGE = "en"
    const val FORCE_COMPACT_TABLES = false
}
