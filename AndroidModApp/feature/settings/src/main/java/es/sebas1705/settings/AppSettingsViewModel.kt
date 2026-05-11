package es.sebas1705.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.authenticationusescases.SignOutUseCase
import es.sebas1705.common.theme.ThemeContrast
import es.sebas1705.core.resources.R
import es.sebas1705.models.AppLanguage
import es.sebas1705.models.SettingsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val readSettingsUseCase: ReadSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppSettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            readSettingsUseCase().collect { settings ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        settings = settings,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun updateMusicVolume(value: Float) = persistSettings {
        it.copy(musicVolume = value)
    }

    fun updateSoundVolume(value: Float) = persistSettings {
        it.copy(soundVolume = value)
    }

    fun updateContrast(contrast: ThemeContrast) = persistSettings {
        it.copy(appContrast = contrast)
    }

    fun setShowTutorialOnNextStart(show: Boolean) = persistSettings {
        it.copy(firstTime = show)
    }

    fun updateLanguage(language: AppLanguage) = persistSettings {
        it.copy(appLanguage = language)
    }

    fun resetDefaults() = persistSettings {
        SettingsModel(
            firstTime = false,
            musicVolume = 1f,
            soundVolume = 1f,
            appContrast = ThemeContrast.Low,
            appLanguage = resolveInitialLanguage(),
        )
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isSignOutLoading = true, errorMessage = null) }

            val didSignOut = signOutUseCase()
            _uiState.update {
                if (didSignOut) {
                    it.copy(isSignOutLoading = false, navigateToLogin = true)
                } else {
                    it.copy(
                        isSignOutLoading = false,
                        errorMessage = context.getString(R.string.core_resources_settings_sign_out_error)
                    )
                }
            }
        }
    }

    fun consumeSignOutNavigation() {
        _uiState.update { it.copy(navigateToLogin = false) }
    }

    private fun persistSettings(transform: (SettingsModel) -> SettingsModel) {
        val current = _uiState.value.settings ?: return
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { updateSettingsUseCase(transform(current)) }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            errorMessage = it.errorMessage
                                ?: context.getString(R.string.core_resources_settings_save_error)
                        )
                    }
                }
        }
    }

    private fun resolveInitialLanguage(): AppLanguage {
        val currentLanguage = context.resources.configuration.locales[0]?.language.orEmpty()
        return if (currentLanguage.equals(AppLanguage.Spanish.code, ignoreCase = true)) {
            AppLanguage.Spanish
        } else {
            AppLanguage.English
        }
    }
}

data class AppSettingsUiState(
    val isLoading: Boolean = true,
    val settings: SettingsModel? = null,
    val isSignOutLoading: Boolean = false,
    val navigateToLogin: Boolean = false,
    val errorMessage: String? = null
)

