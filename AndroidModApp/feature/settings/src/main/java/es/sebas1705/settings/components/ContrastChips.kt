package es.sebas1705.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import es.sebas1705.common.theme.ThemeContrast
import es.sebas1705.core.resources.R

@Composable
internal fun ContrastChips(
    selected: ThemeContrast,
    onSelect: (ThemeContrast) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        ThemeContrast.entries.forEach { contrast ->
            val isSelected = contrast == selected
            AssistChip(
                onClick = { onSelect(contrast) },
                label = {
                    val labelRes = when (contrast) {
                        ThemeContrast.Low -> R.string.core_resources_contrast_low
                        ThemeContrast.Medium -> R.string.core_resources_contrast_medium
                        ThemeContrast.High -> R.string.core_resources_contrast_high
                    }
                    Text(stringResource(labelRes))
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerHigh
                    }
                )
            )
        }
    }
}

