package es.sebas1705.retrofit.opendb.dtos

import com.google.gson.annotations.SerializedName

/**
 * DTO to represent a question from the Opentbd API
 *
 * @property category [String]: Category of the question
 * @property correctAnswer [String]: Correct answer of the question
 * @property difficulty [String]: Difficulty of the question
 * @property incorrectAnswers [List]<[String]>: List of incorrect answers
 * @property question [String]: Question
 * @property type [String]: Type of the question
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
data class QuestionOpendbDto(
    val category: String,
    @SerializedName("correct_answer") val correctAnswer: String,
    val difficulty: String,
    @SerializedName("incorrect_answers") val incorrectAnswers: List<String>,
    val question: String,
    val type: String
)