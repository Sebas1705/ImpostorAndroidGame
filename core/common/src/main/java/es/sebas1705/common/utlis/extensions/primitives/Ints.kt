package es.sebas1705.common.utlis.extensions.primitives

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.ui.unit.Dp

/**
 * Transform a [Int] number to a [Dp] number using the context
 * to get the density of the screen in [Dp]
 *
 * @receiver [Int]: the number in px
 *
 * @param context [Context]: context of the app
 *
 * @return [Dp]: the number in [Dp]
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
fun Int.toDp(context: Context): Dp {
    val density = context.resources.displayMetrics.density
    return Dp(this / density)
}

/**
 * Reduce the number to a string with a letter at the end
 *
 * @receiver [Int]: the number to reduce
 *
 * @return [String]: the number reduced
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@SuppressLint("DefaultLocale")
fun Int.toReducedString(): String {
    return when {
        this >= 1_000_000 -> String.format("%.1fM", this / 1_000_000.0)
        this >= 1_000 -> String.format("%.1fk", this / 1_000.0)
        else -> this.toString()
    }
}
