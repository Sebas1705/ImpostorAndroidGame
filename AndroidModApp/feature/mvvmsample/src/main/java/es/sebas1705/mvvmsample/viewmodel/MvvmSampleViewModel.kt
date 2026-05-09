package es.sebas1705.mvvmsample.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.common.mvvm.MVVMBaseViewModel
import es.sebas1705.settings.GetMyDocsUseCase
import es.sebas1705.settings.InsertDemoMyDocUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * MvvmSampleViewModel
 *
 * @property context [Context]: Application context for the ViewModel.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@HiltViewModel
class MvvmSampleViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMyDocsUseCase: GetMyDocsUseCase,
    private val insertDemoMyDocUseCase: InsertDemoMyDocUseCase,
    @ApplicationContext context: Context
) : MVVMBaseViewModel(context) {

    private val _hello = MutableStateFlow("Hello World")
    val hello
        get() = _hello.asStateFlow()

    private val _couchbaseDocs = MutableStateFlow<List<String>>(emptyList())
    val couchbaseDocs
        get() = _couchbaseDocs.asStateFlow()

    init {
        refreshCouchbaseDocs()
    }

    fun refreshCouchbaseDocs() = execute(Dispatchers.IO) {
        startLoading()
        runCatching {
            val docs = getMyDocsUseCase()
                .sortedByDescending { it.id }

            _hello.value = "Couchbase docs: ${docs.size}"
            _couchbaseDocs.value = docs.take(50).map { "#${it.id} - ${it.name}" }
        }.onFailure {
            stopAndError("Error reading Couchbase docs: ${it.message}")
        }
        stopLoading()
    }

    fun insertDemoDoc() = execute(Dispatchers.IO) {
        startLoading()
        val wasInserted = runCatching {
            insertDemoMyDocUseCase()
        }.getOrElse {
            stopAndError("Error inserting demo doc: ${it.message}")
            false
        }

        if (wasInserted) {
            val docs = getMyDocsUseCase().sortedByDescending { it.id }
            _hello.value = "Couchbase docs: ${docs.size}"
            _couchbaseDocs.value = docs.take(50).map { "#${it.id} - ${it.name}" }
        } else {
            stopAndError("Demo doc was not inserted")
        }
        stopLoading()
    }

}