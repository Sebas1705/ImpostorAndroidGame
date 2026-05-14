package es.sebas1705.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import es.sebas1705.common.utlis.extensions.composables.Compound
import es.sebas1705.common.utlis.extensions.composables.calcLayoutType

@Composable
fun LayoutManager(
    modifier: Modifier = Modifier,
    scrollable: Boolean = false,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    contents: List<@Composable (modifier: Modifier) -> Unit> = emptyList(),
    weights: List<Float?> = contents.map { 1f }
) {
    val type = currentWindowAdaptiveInfo().calcLayoutType()

    val onDynamicColumn: @Composable () -> Unit = {
        DynamicColumn(
            modifier = modifier,
            scrollable = scrollable,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            contents = contents,
            weights = weights
        )
    }

    val onDynamicRow: @Composable () -> Unit = {
        DynamicRow(
            modifier = modifier,
            scrollable = scrollable,
            verticalAlignment = verticalAlignment,
            horizontalArrangement = horizontalArrangement,
            contents = contents,
            weights = weights
        )
    }

    type.Compound(
        onBar = onDynamicColumn,
        onRail = onDynamicColumn,
        onDrawer = onDynamicRow,
        onNone = onDynamicColumn
    )

}

@Composable
private fun DynamicRow(
    modifier: Modifier = Modifier,
    scrollable: Boolean = false,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    contents: List<@Composable (modifier: Modifier) -> Unit> = emptyList(),
    weights: List<Float?> = emptyList(),
) = if (scrollable) LazyRow(
    modifier = modifier,
    horizontalArrangement = horizontalArrangement,
    verticalAlignment = verticalAlignment,
) {
    items(contents.size) {
        contents[it](Modifier.fillMaxHeight())
    }
} else Row(
    modifier = modifier,
    horizontalArrangement = horizontalArrangement,
    verticalAlignment = verticalAlignment,
) {
    contents.forEachIndexed { index, content ->
        val modifier = if(weights[index] != null)
            Modifier.fillMaxHeight().weight(weights[index]!!)
        else Modifier.fillMaxHeight()
        content(modifier)
    }
}

@Composable
private fun DynamicColumn(
    modifier: Modifier = Modifier,
    scrollable: Boolean = false,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    contents: List<@Composable (modifier: Modifier) -> Unit> = emptyList(),
    weights: List<Float?> = emptyList(),
) = if (scrollable) LazyColumn (
    modifier = modifier,
    horizontalAlignment = horizontalAlignment,
    verticalArrangement = verticalArrangement,
) {
    items(contents.size) {
        contents[it](Modifier.fillMaxWidth())
    }
} else Column (
    modifier = modifier,
    horizontalAlignment = horizontalAlignment,
    verticalArrangement = verticalArrangement,
) {
    contents.forEachIndexed { index, content ->
        val modifier = if(weights[index] != null)
            Modifier.fillMaxWidth().weight(weights[index]!!)
        else Modifier.fillMaxWidth()
        content(modifier)
    }
}