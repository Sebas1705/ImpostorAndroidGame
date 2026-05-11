package es.sebas1705.debug

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.core.resources.R
import es.sebas1705.game.GetGameWordsDbStatsUseCase
import es.sebas1705.game.ImportDefaultGameWordsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DebugToolsViewModel @Inject constructor(
    private val getGameWordsDbStatsUseCase: GetGameWordsDbStatsUseCase,
    private val importDefaultGameWordsUseCase: ImportDefaultGameWordsUseCase,
    @ApplicationContext context: Context
) : ViewModel() {

    private val appContext: Context = context

    private val _uiState = MutableStateFlow(DebugToolsUiState())
    val uiState = _uiState.asStateFlow()

    fun refreshAll() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            runCatching {
                val stats = getGameWordsDbStatsUseCase()
                DebugToolsUiState(
                    isLoading = false,
                    selectedLanguage = stats.selectedLanguage,
                    totalWordsEs = stats.totalWordsEs,
                    totalWordsEn = stats.totalWordsEn,
                    selectedLanguageWords = stats.selectedLanguageWords,
                    categoriesEs = stats.categoriesEs,
                    categoriesEn = stats.categoriesEn,
                    categoryCoverageEs = stats.categoryCoverageEs,
                    categoryCoverageEn = stats.categoryCoverageEn,
                    categoryCoverageSelectedLanguage = stats.categoryCoverageSelectedLanguage,
                    averageCluesInSelectedLanguage = stats.averageCluesInSelectedLanguage,
                    latestWordInSelectedLanguage = stats.latestWordInSelectedLanguage,
                    topCategoryInSelectedLanguage = stats.topCategoryInSelectedLanguage
                )
            }.onSuccess {
                _uiState.value = it
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = it.message
                        ?: appContext.getString(R.string.core_resources_debug_error_unknown_diagnostics)
                )
            }
        }
    }

    fun importDefaultWords() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { importDefaultGameWordsUseCase() }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = it.message
                            ?: appContext.getString(R.string.core_resources_debug_error_import_default_words)
                    )
                }
            refreshAll()
        }
    }
}

data class DebugToolsUiState(
    val isLoading: Boolean = false,
    val selectedLanguage: String = "en",
    val totalWordsEs: Int = 0,
    val totalWordsEn: Int = 0,
    val selectedLanguageWords: Int = 0,
    val categoriesEs: Int = 0,
    val categoriesEn: Int = 0,
    val categoryCoverageEs: Double = 0.0,
    val categoryCoverageEn: Double = 0.0,
    val categoryCoverageSelectedLanguage: Double = 0.0,
    val averageCluesInSelectedLanguage: Double = 0.0,
    val latestWordInSelectedLanguage: String? = null,
    val topCategoryInSelectedLanguage: String? = null,
    val errorMessage: String? = null
)

