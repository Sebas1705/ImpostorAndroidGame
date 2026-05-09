package es.sebas1705.common.utlis.extensions.composables

import androidx.compose.ui.graphics.Color

/**
 * Disabled color
 *
 * @receiver [Color]: Color
 *
 * @return [Color]: Disabled color
 *
 * @Version 1.0.0
 * @author Sebas1705 01/03/2025
 */
fun Color.disabled(): Color = copy(alpha = 0.38f)
