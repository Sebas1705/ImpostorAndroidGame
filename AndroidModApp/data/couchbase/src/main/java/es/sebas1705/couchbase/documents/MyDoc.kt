package es.sebas1705.couchbase.documents

import es.sebas1705.couchbase.documents.abstracts.Document

/**
 * MyDoc is a data class that represents a document in Couchbase.
 *
 * @property id [Int] The unique identifier for the document.
 * @property name [String] The name associated with the document.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
data class MyDoc(
    val id: Int,
    val name: String,
) : Document(id.toString())