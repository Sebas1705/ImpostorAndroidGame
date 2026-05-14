package es.sebas1705.settings

import javax.inject.Inject

/**
 * Legacy debug placeholder kept for compatibility after MyDoc repository removal.
 */
data class MyDoc(
    val id: Int,
    val name: String
)

class GetMyDocsUseCase @Inject constructor() {
    suspend operator fun invoke(): List<MyDoc> = emptyList()
}
