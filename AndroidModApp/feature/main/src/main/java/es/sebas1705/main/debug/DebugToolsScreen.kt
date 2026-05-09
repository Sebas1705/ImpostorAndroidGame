package es.sebas1705.main.debug

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.sebas1705.ui.theme.Paddings.MediumPadding

@Composable
fun DebugToolsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    debugToolsViewModel: DebugToolsViewModel = hiltViewModel()
) {
    val uiState by debugToolsViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        debugToolsViewModel.refreshAll()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MediumPadding),
        verticalArrangement = Arrangement.spacedBy(MediumPadding)
    ) {
        Text("Debug tools", style = MaterialTheme.typography.headlineSmall)

        Button(onClick = onBack) {
            Text("Back")
        }

        Button(onClick = debugToolsViewModel::refreshAll) {
            Text("Refresh diagnostics")
        }

        if (uiState.isLoading) {
            Text("Loading diagnostics...")
        }

        Text("Couchbase docs: ${uiState.couchbaseCount}")
        Text("Latest Couchbase doc: ${uiState.latestCouchbaseDoc ?: "No data"}")
        Text("Trivia response count: ${uiState.triviaCount}")
        Text(
            text = "Latest trivia question: ${uiState.latestTriviaQuestion ?: "No data"}",
            modifier = Modifier.fillMaxWidth()
        )

        uiState.errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

