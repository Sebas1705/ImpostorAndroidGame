package es.sebas1705.common.extensions.primitives

import android.content.Context
import androidx.compose.ui.unit.Dp
import es.sebas1705.common.utlis.extensions.primitives.toDp
import es.sebas1705.common.utlis.extensions.primitives.toReducedString
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.*

class IntExtensionsTest {

    @Test fun `toDp converts px to dp correctly`() {
        val displayMetrics = android.util.DisplayMetrics()
        displayMetrics.density = 2.0f
        val mockResources = mock(android.content.res.Resources::class.java)
        `when`(mockResources.displayMetrics).thenReturn(displayMetrics)
        val mockContext = mock(android.content.Context::class.java)
        `when`(mockContext.resources).thenReturn(mockResources)
        val pxValue = 20
        val dpValue = pxValue.toDp(mockContext)
        assertEquals(Dp(10f), dpValue)
    }

    @Test fun `toDp handles zero px value`() {
        val displayMetrics = android.util.DisplayMetrics()
        displayMetrics.density = 2.0f
        val mockResources = mock(android.content.res.Resources::class.java)
        `when`(mockResources.displayMetrics).thenReturn(displayMetrics)
        val mockContext = mock(android.content.Context::class.java)
        `when`(mockContext.resources).thenReturn(mockResources)
        val pxValue = 0
        val dpValue = pxValue.toDp(mockContext)
        assertEquals(Dp(0f), dpValue)
    }

    @Test fun `toDp handles negative px value`() {
        val displayMetrics = android.util.DisplayMetrics()
        displayMetrics.density = 2.0f
        val mockResources = mock(android.content.res.Resources::class.java)
        `when`(mockResources.displayMetrics).thenReturn(displayMetrics)
        val mockContext = mock(android.content.Context::class.java)
        `when`(mockContext.resources).thenReturn(mockResources)
        val pxValue = -20
        val dpValue = pxValue.toDp(mockContext)
        assertEquals(Dp(-10f), dpValue)
    }

    @Test fun `toReducedString formats numbers below 1000 correctly`() {
        val value = 999
        val reducedString = value.toReducedString()
        assertEquals("999", reducedString)
    }

    @Test fun `toReducedString formats numbers in thousands correctly`() {
        val value = 1500
        val reducedString = value.toReducedString()
        assertEquals("1.5k", reducedString)
    }

    @Test fun `toReducedString formats numbers in millions correctly`() {
        val value = 2500000
        val reducedString = value.toReducedString()
        assertEquals("2.5M", reducedString)
    }

    @Test fun `toReducedString handles edge case of exactly 1000`() {
        val value = 1000
        val reducedString = value.toReducedString()
        assertEquals("1.0k", reducedString)
    }

    @Test fun `toReducedString handles edge case of exactly 1000000`() {
        val value = 1000000
        val reducedString = value.toReducedString()
        assertEquals("1.0M", reducedString)
    }
}