package es.sebas1705.mvisample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.sebas1705.mvisample.design.MviSampleDesign
import es.sebas1705.mvisample.viewmodel.MviSampleViewModel

@Composable
fun MviSampleScreen(
    modifier: Modifier = Modifier,
    mviSampleViewModel: MviSampleViewModel = hiltViewModel(),
    onMVVMNav: () -> Unit = {},
) {

    //States:
    val mviSampleState by mviSampleViewModel.uiState.collectAsStateWithLifecycle()

    //Effects:
    @Suppress("EffectKeys")
    LaunchedEffect(Unit) {
    }

    //Body:
    MviSampleDesign(
        modifier = modifier,
        mviSampleState = mviSampleState,
        onMVVMNav = onMVVMNav,
    )
}