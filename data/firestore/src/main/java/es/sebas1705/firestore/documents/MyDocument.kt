package es.sebas1705.firestore.documents

/**
 * Data class to represent the user document in Firestore
 *
 * @property name [String]: name
 * @property age [Int]: age
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
data class MyDocument(
    val name: String = "",
    val age: Int = 0,
)