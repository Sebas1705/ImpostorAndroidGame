package es.sebas1705.mvisample.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.common.mvi.MVIBaseViewModel
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
class MviSampleViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext context: Context
) : MVIBaseViewModel<MviSampleState, MviSampleIntent>(context) {

    override fun initState(): MviSampleState = MviSampleState()

    override fun intentHandler(intent: MviSampleIntent) {
    }



}