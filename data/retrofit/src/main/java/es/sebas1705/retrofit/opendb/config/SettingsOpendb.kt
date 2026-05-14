package es.sebas1705.retrofit.opendb.config

import es.sebas1705.retrofit.opendb.config.SettingsOpendb.TRIVIA_BASE_URL
import es.sebas1705.retrofit.opendb.config.SettingsOpendb.TRIVIA_ENCODE_TEXT
import es.sebas1705.retrofit.opendb.config.SettingsOpendb.TRIVIA_RESPONSE_EXAMPLE
import es.sebas1705.retrofit.opendb.dtos.QuestionOpendbDto
import es.sebas1705.retrofit.opendb.dtos.ResponseOpendbDto

/**
 * Settings of the Opentbd API
 *
 * @property TRIVIA_BASE_URL [String]: Base URL of the Opentbd API
 * @property TRIVIA_ENCODE_TEXT [String]: Text to encode the URL
 * @property TRIVIA_RESPONSE_EXAMPLE [ResponseOpendbDto]: Example of a response from the API
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
object SettingsOpendb {
    const val TRIVIA_BASE_URL = "https://opentdb.com/"
    const val TRIVIA_ENCODE_TEXT = "url3986"

    val TRIVIA_RESPONSE_EXAMPLE = ResponseOpendbDto(
        0,
        (1..10).map {
            QuestionOpendbDto(
                "General Knowledge",
                "hey",
                "Easy",
                listOf("1", "2", "3"),
                "HelloWorld",
                "URL"
            )
        }
    )
}