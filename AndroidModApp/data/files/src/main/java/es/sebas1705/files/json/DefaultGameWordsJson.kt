package es.sebas1705.files.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DefaultGameWordsJson(
    @SerialName("words")
    val words: List<DefaultGameWordJson>
)

@Serializable
data class DefaultGameWordJson(
    val word: String,
    @SerialName("clues")
    val clues: List<String>,
    val category: String
)

data class DefaultGameWordAssetEntry(
    val languageCode: String,
    val word: String,
    val clues: List<String>,
    val category: String
)

