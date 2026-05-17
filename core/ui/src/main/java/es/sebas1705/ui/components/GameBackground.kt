package es.sebas1705.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.cos
import kotlin.math.sin

enum class GameBackgroundStyle {
    Lobby,      // home, mode, settings
    Dramatic,   // splash, login
    Tense,      // reveal phase
    Active,     // discussion phase
    Victory,    // result screen
}

/**
 * Decorative game background that draws ambient shapes on a Canvas
 * using the current Material3 color scheme. Adapts to dark/light mode
 * automatically since all colors are derived from [MaterialTheme.colorScheme].
 */
@Composable
fun GameBackground(
    modifier: Modifier = Modifier,
    style: GameBackgroundStyle = GameBackgroundStyle.Lobby,
    content: @Composable BoxScope.() -> Unit = {},
) {
    val bg = MaterialTheme.colorScheme.background
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val tertiary = MaterialTheme.colorScheme.tertiary

    Box(modifier = modifier.background(bg)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            when (style) {
                GameBackgroundStyle.Lobby -> drawLobby(primary, secondary, tertiary)
                GameBackgroundStyle.Dramatic -> drawDramatic(primary, secondary, tertiary)
                GameBackgroundStyle.Tense -> drawTense(primary, secondary)
                GameBackgroundStyle.Active -> drawActive(primary, secondary, tertiary)
                GameBackgroundStyle.Victory -> drawVictory(primary, secondary, tertiary)
            }
        }
        content()
    }
}

// ─── Style implementations ────────────────────────────────────────────────────

private fun DrawScope.drawLobby(primary: Color, secondary: Color, tertiary: Color) {
    val dim = size.minDimension

    // Large primary glow — top-right
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(primary.copy(alpha = 0.14f), Color.Transparent),
            center = Offset(size.width * 0.95f, size.height * 0.05f),
            radius = dim * 0.72f,
        ),
        size = size,
    )
    // Large secondary glow — bottom-left
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(secondary.copy(alpha = 0.11f), Color.Transparent),
            center = Offset(size.width * 0.05f, size.height * 0.92f),
            radius = dim * 0.60f,
        ),
        size = size,
    )
    // Small tertiary accent mid-left
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(tertiary.copy(alpha = 0.07f), Color.Transparent),
            center = Offset(size.width * 0.10f, size.height * 0.40f),
            radius = dim * 0.28f,
        ),
        size = size,
    )
    // Outline diamonds
    val stroke = 1.5f
    drawDiamond(Offset(size.width * 0.88f, size.height * 0.14f), dim * 0.042f, tertiary.copy(alpha = 0.20f), stroke)
    drawDiamond(Offset(size.width * 0.12f, size.height * 0.80f), dim * 0.058f, primary.copy(alpha = 0.14f), stroke)
    drawDiamond(Offset(size.width * 0.72f, size.height * 0.90f), dim * 0.032f, secondary.copy(alpha = 0.18f), stroke)
    drawDiamond(Offset(size.width * 0.55f, size.height * 0.06f), dim * 0.026f, primary.copy(alpha = 0.13f), stroke)
}

private fun DrawScope.drawDramatic(primary: Color, secondary: Color, tertiary: Color) {
    val dim = size.minDimension

    // Central radial glow from upper third — like a stage spotlight
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(primary.copy(alpha = 0.18f), Color.Transparent),
            center = Offset(size.width * 0.50f, size.height * 0.18f),
            radius = dim * 0.82f,
        ),
        size = size,
    )
    // Concentric rings (radar / target effect) — centred on the logo area
    val ringCenter = Offset(size.width * 0.50f, size.height * 0.38f)
    drawCircle(center = ringCenter, radius = dim * 0.22f, color = primary.copy(alpha = 0.08f), style = Stroke(width = 1.5f))
    drawCircle(center = ringCenter, radius = dim * 0.38f, color = primary.copy(alpha = 0.05f), style = Stroke(width = 1.5f))
    drawCircle(center = ringCenter, radius = dim * 0.56f, color = primary.copy(alpha = 0.03f), style = Stroke(width = 1.5f))

    // Bottom secondary warmth
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(secondary.copy(alpha = 0.10f), Color.Transparent),
            center = Offset(size.width * 0.50f, size.height * 1.05f),
            radius = dim * 0.58f,
        ),
        size = size,
    )
    // Corner hexagons
    val stroke = 1.5f
    drawHexagon(Offset(size.width * 0.08f, size.height * 0.07f), dim * 0.058f, tertiary.copy(alpha = 0.16f), stroke)
    drawHexagon(Offset(size.width * 0.92f, size.height * 0.93f), dim * 0.048f, secondary.copy(alpha = 0.13f), stroke)
    drawHexagon(Offset(size.width * 0.90f, size.height * 0.12f), dim * 0.034f, primary.copy(alpha = 0.10f), stroke)
}

private fun DrawScope.drawTense(primary: Color, secondary: Color) {
    val dim = size.minDimension

    // Top vertical gradient — "danger zone" feeling
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(primary.copy(alpha = 0.14f), Color.Transparent),
            startY = 0f,
            endY = size.height * 0.45f,
        ),
        size = size,
    )
    // Side edge glows (soft vignette)
    drawRect(
        brush = Brush.horizontalGradient(
            colors = listOf(primary.copy(alpha = 0.07f), Color.Transparent),
            startX = 0f,
            endX = size.width * 0.32f,
        ),
        size = size,
    )
    drawRect(
        brush = Brush.horizontalGradient(
            colors = listOf(Color.Transparent, primary.copy(alpha = 0.07f)),
            startX = size.width * 0.68f,
            endX = size.width,
        ),
        size = size,
    )
    // Faint central secondary glow (the "word" feeling)
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(secondary.copy(alpha = 0.09f), Color.Transparent),
            center = Offset(size.width * 0.50f, size.height * 0.55f),
            radius = dim * 0.50f,
        ),
        size = size,
    )
    // Hexagonal tension markers
    val stroke = 1.5f
    drawHexagon(Offset(size.width * 0.06f, size.height * 0.16f), dim * 0.070f, primary.copy(alpha = 0.13f), stroke)
    drawHexagon(Offset(size.width * 0.94f, size.height * 0.22f), dim * 0.052f, secondary.copy(alpha = 0.11f), stroke)
    drawHexagon(Offset(size.width * 0.84f, size.height * 0.82f), dim * 0.060f, primary.copy(alpha = 0.09f), stroke)
    drawHexagon(Offset(size.width * 0.16f, size.height * 0.78f), dim * 0.044f, secondary.copy(alpha = 0.09f), stroke)
}

private fun DrawScope.drawActive(primary: Color, secondary: Color, tertiary: Color) {
    val dim = size.minDimension

    // Multiple scattered energy glows — like voices / debate bubbles
    data class Blob(val cx: Float, val cy: Float, val r: Float, val c: Color)

    val blobs = listOf(
        Blob(size.width * 0.14f, size.height * 0.11f, dim * 0.18f, primary),
        Blob(size.width * 0.82f, size.height * 0.09f, dim * 0.22f, secondary),
        Blob(size.width * 0.04f, size.height * 0.52f, dim * 0.14f, tertiary),
        Blob(size.width * 0.96f, size.height * 0.58f, dim * 0.17f, primary),
        Blob(size.width * 0.22f, size.height * 0.88f, dim * 0.20f, secondary),
        Blob(size.width * 0.72f, size.height * 0.90f, dim * 0.14f, tertiary),
        Blob(size.width * 0.50f, size.height * 0.04f, dim * 0.12f, primary),
        Blob(size.width * 0.60f, size.height * 0.50f, dim * 0.10f, secondary),
    )
    blobs.forEach { b ->
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(b.c.copy(alpha = 0.09f), Color.Transparent),
                center = Offset(b.cx, b.cy),
                radius = b.r * 2.4f,
            ),
            size = size,
        )
        drawCircle(color = b.c.copy(alpha = 0.07f), radius = b.r, center = Offset(b.cx, b.cy), style = Stroke(width = 1.5f))
    }
    // Bottom gradient band (energy pooling)
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color.Transparent, secondary.copy(alpha = 0.07f)),
            startY = size.height * 0.72f,
            endY = size.height,
        ),
        size = size,
    )
}

private fun DrawScope.drawVictory(primary: Color, secondary: Color, tertiary: Color) {
    val dim = size.minDimension
    val center = Offset(size.width * 0.50f, size.height * 0.50f)

    // Large celebratory central glow
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(secondary.copy(alpha = 0.16f), Color.Transparent),
            center = center,
            radius = dim * 0.72f,
        ),
        size = size,
    )
    // Radial burst lines
    val lineCount = 14
    val inner = dim * 0.13f
    for (i in 0 until lineCount) {
        val angle = (2.0 * Math.PI / lineCount * i).toFloat()
        val outer = if (i % 2 == 0) dim * 0.48f else dim * 0.30f
        drawLine(
            color = primary.copy(alpha = 0.07f),
            start = Offset(center.x + inner * cos(angle), center.y + inner * sin(angle)),
            end = Offset(center.x + outer * cos(angle), center.y + outer * sin(angle)),
            strokeWidth = 2.0f,
            cap = StrokeCap.Round,
        )
    }
    // Ring of small filled dots
    val dotCount = 9
    val ringR = dim * 0.40f
    for (i in 0 until dotCount) {
        val angle = (2.0 * Math.PI / dotCount * i).toFloat()
        drawCircle(
            color = secondary.copy(alpha = 0.16f),
            radius = dim * 0.017f,
            center = Offset(center.x + ringR * cos(angle), center.y + ringR * sin(angle)),
        )
    }
    // Corner tertiary accents
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(tertiary.copy(alpha = 0.09f), Color.Transparent),
            center = Offset(0f, 0f),
            radius = dim * 0.42f,
        ),
        size = size,
    )
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(tertiary.copy(alpha = 0.09f), Color.Transparent),
            center = Offset(size.width, size.height),
            radius = dim * 0.42f,
        ),
        size = size,
    )
}

// ─── Shape helpers ────────────────────────────────────────────────────────────

private fun DrawScope.drawDiamond(center: Offset, halfSize: Float, color: Color, strokeWidth: Float) {
    val path = Path().apply {
        moveTo(center.x, center.y - halfSize)
        lineTo(center.x + halfSize * 0.65f, center.y)
        lineTo(center.x, center.y + halfSize)
        lineTo(center.x - halfSize * 0.65f, center.y)
        close()
    }
    drawPath(path, color = color, style = Stroke(width = strokeWidth))
}

private fun DrawScope.drawHexagon(center: Offset, radius: Float, color: Color, strokeWidth: Float) {
    val path = Path()
    for (i in 0..5) {
        val angle = (Math.PI / 3.0 * i - Math.PI / 6.0).toFloat()
        val x = center.x + radius * cos(angle)
        val y = center.y + radius * sin(angle)
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, color = color, style = Stroke(width = strokeWidth))
}
