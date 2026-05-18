package es.sebas1705.home.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import es.sebas1705.core.resources.R

/**
 * Profile screen header: banner image with a floating circular avatar and
 * the user's Google display name + email address beneath it.
 *
 * @param displayName  Google display name; shows a placeholder when null.
 * @param email        Account e-mail address; hidden when null or blank.
 * @param photoUrl     HTTPS URL of the Google profile photo; falls back to an icon.
 */
@Composable
internal fun ProfileHeaderCard(
    displayName: String?,
    email: String?,
    photoUrl: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        // ── Banner + floating avatar ───────────────────────────────────────
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            // Banner image card — leaves room at the bottom for the avatar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 44.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                ) {
                    Image(
                        painter = painterResource(R.drawable.core_resources_il_wordbank_banner),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
            }

            // Circular profile photo with a surface-coloured border so it
            // visually "floats" over the banner card edge.
            Surface(
                shape = CircleShape,
                modifier = Modifier.size(88.dp),
                border = BorderStroke(3.dp, MaterialTheme.colorScheme.background),
                color = MaterialTheme.colorScheme.surfaceVariant,
            ) {
                if (!photoUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(photoUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = displayName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }

        // ── Name ──────────────────────────────────────────────────────────
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = displayName ?: "—",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        // ── Email ─────────────────────────────────────────────────────────
        if (!email.isNullOrBlank()) {
            Text(
                text = email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}
