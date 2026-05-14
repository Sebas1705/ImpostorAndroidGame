package es.sebas1705.login.design

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.sebas1705.common.utlis.UiModePreviews
import es.sebas1705.ui.theme.AppTheme
import es.sebas1705.core.resources.R as ResourceR

@Composable
fun LoginDesign(
    modifier: Modifier = Modifier,
    isCheckingSession: Boolean = false,
    loading: Boolean = false,
    errorMessage: String? = null,
    onGoogleSignIn: () -> Unit = {},
    onDismissError: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(ResourceR.drawable.core_resources_ic_app_logo),
            contentDescription = stringResource(ResourceR.string.core_resources_icon_content),
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .fillMaxHeight(0.4f)
                .padding(bottom = 6.dp)
        )

        if (isCheckingSession || loading)
            CircularProgressIndicator(
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 6.dp),
                strokeWidth = 8.dp
            )

        else {
            Text(
                text = stringResource(ResourceR.string.core_resources_login_welcome_back),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = stringResource(ResourceR.string.core_resources_login_google_prompt),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Button(
            onClick = onGoogleSignIn,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (loading) {
                    stringResource(ResourceR.string.core_resources_login_signing_in)
                } else {
                    stringResource(ResourceR.string.core_resources_login_continue_google)
                }
            )
        }

        if (!errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
            OutlinedButton(onClick = onDismissError, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(ResourceR.string.core_resources_dismiss))
            }
        }
    }
}

@UiModePreviews
@Composable
private fun Preview() {
    AppTheme {
        LoginDesign()
    }
}
