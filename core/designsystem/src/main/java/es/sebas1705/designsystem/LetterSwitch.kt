package es.sebas1705.designsystem

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun LetterSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    letterOn: String = "A",
    letterOff: String = "B"
) {
    val switchWidth = 52.dp
    val switchHeight = 32.dp
    val circleSize = 24.dp
    val lateralPadding = 4.dp
    val circleOffset by animateDpAsState(
        if(checked) switchWidth - circleSize - lateralPadding else lateralPadding,
        label = "circleOffset"
    )

    Surface(
        modifier = modifier
            .size(switchWidth, switchHeight),
        color = MaterialTheme.colorScheme.let {
            if(checked)
                it.primary
            else it.surfaceContainerHighest
        },
        shape = CircleShape,
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.let {
                if(checked)
                    it.surfaceContainerHighest
                else it.outline
            }
        ),
        onClick = {
            onCheckedChange(!checked)
        }
    ) {
        Box(
            Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(circleOffset.value.toInt(), 0) }
                    .size(circleSize)
                    .background(
                        MaterialTheme.colorScheme.let {
                            if (checked) it.onPrimaryContainer
                            else it.outline
                        },
                        CircleShape
                    )
                    .align(Alignment.CenterStart),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (checked) letterOn else letterOff,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.let { if (checked) it.onPrimary else it.primary }
                )
            }
        }
    }
}