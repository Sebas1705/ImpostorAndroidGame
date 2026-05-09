package es.sebas1705.files.json

import kotlinx.serialization.Serializable

/**
 * MyJson
 *
 * @property name [String]: name
 * @property age [Int]: age
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@Serializable
data class MyJson(
    val name: String,
    val age: Int,
)