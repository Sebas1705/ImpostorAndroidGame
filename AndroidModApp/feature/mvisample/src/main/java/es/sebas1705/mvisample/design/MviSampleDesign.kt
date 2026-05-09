package es.sebas1705.mvisample.design

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import es.sebas1705.common.utlis.UiModePreviews
import es.sebas1705.mvisample.viewmodel.MviSampleState
import es.sebas1705.ui.theme.AppTheme
import es.sebas1705.ui.theme.Paddings.MediumPadding

/**
 * Design of the MviSample screen
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@Composable
fun MviSampleDesign(
    modifier: Modifier = Modifier,
    mviSampleState: MviSampleState = MviSampleState(),
    onMVVMNav: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MediumPadding)
        ) {
            FilledTonalButton(
                onClick = onMVVMNav
            ) {
                Text("Go to MVVM Sample: ${mviSampleState.helloWorld}")
            }
        }
    }
}


@UiModePreviews
@Composable
private fun Preview() {
    AppTheme {
        MviSampleDesign()
    }
}