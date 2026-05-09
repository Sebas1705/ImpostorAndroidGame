package es.sebas1705.main.debug

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.sebas1705.opendbusescases.GetTriviaTenQuestionsUseCase
import es.sebas1705.settings.GetMyDocsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DebugToolsViewModel @Inject constructor(
    private val getMyDocsUseCase: GetMyDocsUseCase,
    private val getTriviaTenQuestionsUseCase: GetTriviaTenQuestionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DebugToolsUiState())
    val uiState = _uiState.asStateFlow()

    fun refreshAll() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            runCatching {
                val couchbaseDocs = getMyDocsUseCase().sortedByDescending { it.id }
                val trivia = getTriviaTenQuestionsUseCase()
                DebugToolsUiState(
                    isLoading = false,
                    couchbaseCount = couchbaseDocs.size,
                    latestCouchbaseDoc = couchbaseDocs.firstOrNull()?.let { "#${it.id} - ${it.name}" },
                    triviaCount = trivia.questionOpendbDtos.size,
                    latestTriviaQuestion = trivia.questionOpendbDtos.firstOrNull()?.question
                )
            }.onSuccess {
                _uiState.value = it
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = it.message ?: "Unknown diagnostics error"
                )
            }
        }
    }
}

data class DebugToolsUiState(
    val isLoading: Boolean = false,
    val couchbaseCount: Int = 0,
    val latestCouchbaseDoc: String? = null,
    val triviaCount: Int = 0,
    val latestTriviaQuestion: String? = null,
    val errorMessage: String? = null
)

