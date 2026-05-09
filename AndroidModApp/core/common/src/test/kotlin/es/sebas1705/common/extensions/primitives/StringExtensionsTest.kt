package es.sebas1705.common.extensions.primitives

import es.sebas1705.common.utlis.extensions.primitives.decodeUrl
import es.sebas1705.common.utlis.extensions.primitives.normalizeString
import org.junit.Assert.assertEquals
import org.junit.Test

class StringExtensionsTest {

    @Test fun `normalizeString removes diacritical marks and converts to lowercase`() {
        val input = "ÁÉÍÓÚáéíóúÑñ"
        val normalized = input.normalizeString()
        assertEquals("aeiouaeiounn", normalized)
    }

    @Test fun `normalizeString handles empty string`() {
        val input = ""
        val normalized = input.normalizeString()
        assertEquals("", normalized)
    }

    @Test fun `normalizeString handles string without diacritical marks`() {
        val input = "Hello World"
        val normalized = input.normalizeString()
        assertEquals("hello world", normalized)
    }

    @Test fun `decodeUrl decodes URL encoded string correctly`() {
        val input = "Hello%20World%21"
        val decoded = input.decodeUrl()
        assertEquals("Hello World!", decoded)
    }

    @Test fun `decodeUrl handles empty string`() {
        val input = ""
        val decoded = input.decodeUrl()
        assertEquals("", decoded)
    }

    @Test fun `decodeUrl handles already decoded string`() {
        val input = "Hello World!"
        val decoded = input.decodeUrl()
        assertEquals("Hello World!", decoded)
    }
}