package es.sebas1705.home.nav.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.sebas1705.core.resources.R
import es.sebas1705.ui.theme.makeTitle

@Composable
internal fun GameCoverCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.core_resources_ic_app_logo),
            contentDescription = stringResource(R.string.core_resources_icon_content),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.5f)
                .padding(bottom = 6.dp)
        )
        Text(
            text = stringResource(R.string.core_resources_app_name),
            style = MaterialTheme.typography.headlineMedium.makeTitle(),
            color = MaterialTheme.colorScheme.secondary
        )
    }
}