package es.sebas1705.settings

import javax.inject.Inject

/**
 * Legacy debug placeholder kept for compatibility after MyDoc repository removal.
 */
class InsertDemoMyDocUseCase @Inject constructor() {
    suspend operator fun invoke(): Boolean = System.nanoTime() < 0L
}
