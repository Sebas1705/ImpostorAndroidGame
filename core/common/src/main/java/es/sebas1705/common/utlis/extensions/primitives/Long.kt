package es.sebas1705.common.utlis.extensions.primitives

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Format a long number to a date string
 *
 * @receiver [Long]: the number to format
 *
 * @return [String]: the date formatted
 *
 * @see DateTimeFormatter
 * @see ZoneId
 * @see Instant
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
fun Long.millisToFormatDate(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")
        .withZone(ZoneId.systemDefault())
    return formatter.format(Instant.ofEpochMilli(this))
}