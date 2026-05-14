package es.sebas1705.common.utlis.extensions.primitives

/**
 * Adds an element to the end of the list and executes pre-actions if provided.
 *
 * @receiver [MutableList]<T>: The list to which the element will be added.
 * @param element T: The element to be added.
 * @param preActions (MutableList<T>) -> Unit: A lambda function that will be executed before adding the element.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
fun <T> MutableList<T>.push(element: T, preActions: (MutableList<T>) -> Unit = {}) {
    preActions(this)
    this.add(this.size, element)
}

/**
 * Removes and returns the last element of the list.
 *
 * @receiver [MutableList]<T>: The list from which the last element will be removed.
 * @return T?: The last element of the list, or null if the list is empty.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
fun <T> MutableList<T>.pop() = if (this.isNotEmpty()) this.removeAt(this.size - 1) else null

/**
 * Adds an element to the end of the list if it is not already present.
 *
 * @receiver [MutableList]<T>: The list to which the element will be added.
 * @param element T: The element to be added.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
fun <T> MutableList<T>.pushAndPopTo(
    element: T,
    to: T,
    inclusive: Boolean = false
) = this.push(element) {
    while (this.lastOrNull() != to && this.isNotEmpty()) {
        this.pop()
    }
    if (inclusive)
        this.pop()
}

/**
 * Clears the list and adds a single element to it.
 *
 * @receiver [MutableList]<T>: The list to be cleared and updated.
 * @param element T: The element to be added after clearing the list.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
fun <T> MutableList<T>.pushAndFree(
    element: T
) = this.push(element) {
    this.clear()
}