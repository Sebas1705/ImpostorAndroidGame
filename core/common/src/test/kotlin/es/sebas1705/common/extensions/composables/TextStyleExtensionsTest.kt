package es.sebas1705.common.extensions.composables

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import es.sebas1705.common.utlis.extensions.composables.makeBold
import es.sebas1705.common.utlis.extensions.composables.makeItalic
import org.junit.Assert.assertEquals
import org.junit.Test

class TextStyleExtensionsTest {

    @Test
    fun `makeBold sets font weight to bold`() {
        val originalStyle = TextStyle(fontWeight = FontWeight.Normal)
        val boldStyle = originalStyle.makeBold()
        assertEquals(FontWeight.Bold, boldStyle.fontWeight)
    }

    @Test
    fun `makeBold preserves other text style properties`() {
        val originalStyle = TextStyle(fontWeight = FontWeight.Normal, fontStyle = FontStyle.Italic)
        val boldStyle = originalStyle.makeBold()
        assertEquals(FontStyle.Italic, boldStyle.fontStyle!!)
    }

    @Test
    fun `makeItalic sets font style to italic`() {
        val originalStyle = TextStyle(fontStyle = FontStyle.Normal)
        val italicStyle = originalStyle.makeItalic()
        assertEquals(FontStyle.Italic, italicStyle.fontStyle!!)
    }

    @Test
    fun `makeItalic preserves other text style properties`() {
        val originalStyle = TextStyle(fontStyle = FontStyle.Normal, fontWeight = FontWeight.Bold)
        val italicStyle = originalStyle.makeItalic()
        assertEquals(FontWeight.Bold, italicStyle.fontWeight)
    }
}