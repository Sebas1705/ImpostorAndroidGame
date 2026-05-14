package es.sebas1705.common.extensions.primitives

import es.sebas1705.common.utlis.extensions.primitives.pop
import es.sebas1705.common.utlis.extensions.primitives.push
import es.sebas1705.common.utlis.extensions.primitives.pushAndFree
import es.sebas1705.common.utlis.extensions.primitives.pushAndPopTo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ListExtensionsTest {

    @Test
    fun `push adds element to the end of the list`() {
        val list = mutableListOf(1, 2, 3)
        list.push(4)
        assertEquals(listOf(1, 2, 3, 4), list)
    }

    @Test fun `push executes preActions before adding element`() {
        val list = mutableListOf(1, 2, 3)
        list.push(4) { it.clear() }
        assertEquals(listOf(4), list)
    }

    @Test fun `pop removes and returns the last element`() {
        val list = mutableListOf(1, 2, 3)
        val popped = list.pop()
        assertEquals(3, popped)
        assertEquals(listOf(1, 2), list)
    }

    @Test fun `pop returns null for empty list`() {
        val list = mutableListOf<Int>()
        val popped = list.pop()
        assertNull(popped)
    }

    @Test fun `pushAndPopTo removes elements until target is found`() {
        val list = mutableListOf(1, 2, 3, 4, 5)
        list.pushAndPopTo(6, 3)
        assertEquals(listOf(1, 2, 3, 6), list)
    }

    @Test fun `pushAndPopTo removes elements inclusively`() {
        val list = mutableListOf(1, 2, 3, 4, 5)
        list.pushAndPopTo(6, 3, inclusive = true)
        assertEquals(listOf(1, 2, 6), list)
    }

    @Test fun `pushAndPopTo does nothing if target is not found`() {
        val list = mutableListOf(1, 2, 3)
        list.pushAndPopTo(4, 5)
        assertEquals(listOf(4), list)
    }

    @Test fun `pushAndFree clears list and adds element`() {
        val list = mutableListOf(1, 2, 3)
        list.pushAndFree(4)
        assertEquals(listOf(4), list)
    }
}