package es.sebas1705.splash.design

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import es.sebas1705.common.utlis.UiModePreviews
import es.sebas1705.ui.theme.AppTheme
import es.sebas1705.ui.theme.Paddings.SmallPadding

/**
 * Splash screen design of the app
 *
 * @since 0.1.0
 * @author Sebastian Ramiro Entrerrios García
 */
@Composable
fun SplashDesign(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(es.sebas1705.core.resources.R.drawable.core_resources_ic_android_black_24dp),
            contentDescription = stringResource(es.sebas1705.core.resources.R.string.core_resources_app_name),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight(0.4f)
        )
        Spacer(Modifier.height(SmallPadding))
        Text(stringResource(es.sebas1705.core.resources.R.string.core_resources_app_name))
        Spacer(Modifier.height(SmallPadding))
        LinearProgressIndicator()
    }
}

@UiModePreviews
@Composable
private fun Preview() {
    AppTheme {
        SplashDesign()
    }
}