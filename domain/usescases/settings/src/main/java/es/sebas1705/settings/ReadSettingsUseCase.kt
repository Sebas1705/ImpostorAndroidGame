package es.sebas1705.settings

import es.sebas1705.mappers.toModel
import es.sebas1705.models.SettingsModel
import es.sebas1705.repositories.interfaces.IAppSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReadSettingsUseCase @Inject constructor(
    private val settingsRepository: IAppSettingsRepository
) {
    /**
     * Reads the current settings
     *
     * @return [Flow]<[SettingsModel]> Flow that emits the current settings model.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    operator fun invoke(): Flow<SettingsModel> =
        settingsRepository.read().map { it.toModel() }
}