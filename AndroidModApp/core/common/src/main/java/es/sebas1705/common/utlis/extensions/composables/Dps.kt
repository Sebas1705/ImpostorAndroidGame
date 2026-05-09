package es.sebas1705.common.utlis.extensions.composables

import android.content.Context
import androidx.compose.ui.unit.Dp

/**
 * Transform a [Dp] number to a [Int] number using the context
 * to get the density of the screen in [Dp]
 *
 * @receiver [Dp]: the number in [Dp]
 *
 * @param context [Context]: context of the app
 *
 * @return [Int]: the number in px
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
fun Dp.toPx(context: Context): Int {
    val density = context.resources.displayMetrics.density
    return (this.value * density).toInt()
}