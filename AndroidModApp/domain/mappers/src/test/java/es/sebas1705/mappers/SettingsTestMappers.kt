package es.sebas1705.mappers

import es.sebas1705.common.theme.ThemeContrast
import es.sebas1705.datastore.model.SettingsData
import es.sebas1705.models.SettingsModel
import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsTestMappers {

    @Test
    fun `SettingsData to SettingsModel conversion`() {
        val settingsData = SettingsData(
            firstTime = false,
            musicVolume = 0.5f,
            soundVolume = 0.5f,
            appContrast = ThemeContrast.Low.ordinal,
            defaultSet = true
        )

        val settingsModel = settingsData.toModel()

        assertEquals(settingsData.firstTime, settingsModel.firstTime)
        assertEquals(settingsData.musicVolume, settingsModel.musicVolume)
        assertEquals(settingsData.soundVolume, settingsModel.soundVolume)
        assertEquals(ThemeContrast.entries[settingsData.appContrast], settingsModel.appContrast)
    }

    @Test
    fun `SettingsModel to SettingsData conversion`() {
        val settingsModel = SettingsModel(
            firstTime = false,
            musicVolume = 0.5f,
            soundVolume = 0.5f,
            appContrast = ThemeContrast.Low
        )

        val settingsData = settingsModel.toData()

        assertEquals(settingsModel.firstTime, settingsData.firstTime)
        assertEquals(settingsModel.musicVolume, settingsData.musicVolume)
        assertEquals(settingsModel.soundVolume, settingsData.soundVolume)
        assertEquals(settingsModel.appContrast.ordinal, settingsData.appContrast)
    }
}