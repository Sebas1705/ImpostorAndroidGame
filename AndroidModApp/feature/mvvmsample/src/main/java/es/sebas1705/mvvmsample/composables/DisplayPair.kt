package es.sebas1705.mvvmsample.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import es.sebas1705.common.utlis.ComposablePreview
import es.sebas1705.common.utlis.extensions.composables.makeBold
import es.sebas1705.ui.theme.Paddings.SmallPadding

/**
 * Display a pair of strings
 *
 * @param pair [Pair] The pair to display
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@Composable
fun DisplayPair(
    pair: Pair<String, String>,
    textColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Row {
        Text(
            text = pair.first,
            style = MaterialTheme.typography.headlineSmall.makeBold(),
            modifier = Modifier.padding(end = SmallPadding),
            color = textColor
        )
        Text(
            text = pair.second,
            style = MaterialTheme.typography.headlineSmall,
            color = textColor
        )
    }
}

@ComposablePreview
@Composable
fun DisplayPairPreview() {
    DisplayPair(
        pair = Pair("Key", "Value")
    )
}