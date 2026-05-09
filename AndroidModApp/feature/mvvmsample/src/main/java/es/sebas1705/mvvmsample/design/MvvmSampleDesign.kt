package es.sebas1705.mvvmsample.design

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import es.sebas1705.common.utlis.UiModePreviews
import es.sebas1705.ui.theme.AppTheme
import es.sebas1705.ui.theme.Paddings.MediumPadding

/**
 * Design of the MvvmSample screen
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@Composable
fun MvvmSampleDesign(
    modifier: Modifier = Modifier,
    hello: String = "Hello World",
    loading: Boolean = false,
    couchbaseDocs: List<String> = emptyList(),
    onRefreshCouchbase: () -> Unit = {},
    onInsertDemoDoc: () -> Unit = {},
    showDebugActions: Boolean = false,
    onDebugNav: () -> Unit = {},
    onMVINav: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MediumPadding)
        ) {
            Text(hello)

            FilledTonalButton(
                onClick = onMVINav
            ) {
                Text("Go to MVI Sample")
            }

            if (showDebugActions) {
                FilledTonalButton(onClick = onDebugNav) {
                    Text("Open debug tools")
                }
            }

            FilledTonalButton(onClick = onRefreshCouchbase) {
                Text("Refresh Couchbase")
            }

            FilledTonalButton(onClick = onInsertDemoDoc) {
                Text("Insert demo Couchbase doc")
            }

            if (loading) {
                Text("Loading...")
            }

            if (couchbaseDocs.isEmpty()) {
                Text("No Couchbase docs yet")
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = MediumPadding * 24)
                ) {
                    items(couchbaseDocs) { doc ->
                        Text(doc)
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}


@UiModePreviews
@Composable
private fun Preview() {
    AppTheme {
        MvvmSampleDesign()
    }
}