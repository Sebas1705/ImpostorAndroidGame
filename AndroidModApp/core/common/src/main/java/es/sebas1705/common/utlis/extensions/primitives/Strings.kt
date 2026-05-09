package es.sebas1705.common.utlis.extensions.primitives

import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.text.Normalizer

/**
 * Normalize a string
 *
 * @receiver [String]: the string to normalize
 *
 * @return [String]: the string normalized
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
fun String.normalizeString(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
    return normalized.replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "").lowercase()
}

/**
 * Decode a string from URL format using [URLDecoder.decode]
 * and standard charset [StandardCharsets.UTF_8]
 *
 * @receiver [String] to decode
 *
 * @return [String] decoded
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
fun String.decodeUrl(): String {
    return URLDecoder.decode(this, StandardCharsets.UTF_8.toString())
}
