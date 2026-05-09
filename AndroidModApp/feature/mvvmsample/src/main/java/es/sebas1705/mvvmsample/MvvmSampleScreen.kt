package es.sebas1705.mvvmsample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.sebas1705.mvvmsample.design.MvvmSampleDesign
import es.sebas1705.mvvmsample.viewmodel.MvvmSampleViewModel

@Composable
fun MvvmSampleScreen(
    modifier: Modifier = Modifier,
    mvvmSampleViewModel: MvvmSampleViewModel = hiltViewModel(),
    showDebugActions: Boolean = false,
    onMVINav: () -> Unit = {},
    onDebugNav: () -> Unit = {},
) {

    //States:
    val hello by mvvmSampleViewModel.hello.collectAsStateWithLifecycle()
    val loading by mvvmSampleViewModel.loading.collectAsStateWithLifecycle()
    val couchbaseDocs by mvvmSampleViewModel.couchbaseDocs.collectAsStateWithLifecycle()

    //Effects:
    @Suppress("EffectKeys")
    LaunchedEffect(Unit) {
    }

    //Body:
    MvvmSampleDesign(
        modifier = modifier,
        hello = hello,
        loading = loading,
        couchbaseDocs = couchbaseDocs,
        onRefreshCouchbase = mvvmSampleViewModel::refreshCouchbaseDocs,
        onInsertDemoDoc = mvvmSampleViewModel::insertDemoDoc,
        showDebugActions = showDebugActions,
        onDebugNav = onDebugNav,
        onMVINav = onMVINav
    )
}