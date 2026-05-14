package es.sebas1705.common.extensions.composables

import androidx.compose.ui.graphics.Color
import es.sebas1705.common.utlis.extensions.composables.disabled
import org.junit.Assert.assertEquals
import org.junit.Test

class ColorExtensionsTest {

    @Test
    fun `disabled reduces alpha to 0_38`() {
        val originalColor = Color(0xFF0000FF) // Blue with full alpha
        val disabledColor = originalColor.disabled()
        assertEquals(0.38f, disabledColor.alpha, 0.1f)
    }

    @Test
    fun `disabled maintains RGB values`() {
        val originalColor = Color(0xFF00FF00) // Green with full alpha
        val disabledColor = originalColor.disabled()
        assertEquals(originalColor.red, disabledColor.red)
        assertEquals(originalColor.green, disabledColor.green)
        assertEquals(originalColor.blue, disabledColor.blue)
    }

    @Test
    fun `disabled works with transparent color`() {
        val originalColor = Color(0x00000000) // Fully transparent black
        val disabledColor = originalColor.disabled()
        assertEquals(0.0f, disabledColor.red)
        assertEquals(0.0f, disabledColor.green)
        assertEquals(0.0f, disabledColor.blue)
        assertEquals(0.38f, disabledColor.alpha, 0.1f)
    }
}