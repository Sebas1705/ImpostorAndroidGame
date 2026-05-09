package es.sebas1705.common.utlis.extensions.composables

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

/**
 * Make the text style bold
 *
 * @receiver [TextStyle]: Text style
 *
 * @return [TextStyle]: Text style with bold
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
fun TextStyle.makeBold(): TextStyle = this.copy(fontWeight = FontWeight.Bold)

/**
 * Make the text style italic
 *
 * @receiver [TextStyle]: Text style
 *
 * @return [TextStyle]: Text style with italic
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
fun TextStyle.makeItalic(): TextStyle = this.copy(fontStyle = FontStyle.Italic)


