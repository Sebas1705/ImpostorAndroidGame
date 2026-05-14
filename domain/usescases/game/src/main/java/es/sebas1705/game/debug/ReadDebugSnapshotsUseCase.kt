package es.sebas1705.game.debug

import es.sebas1705.models.DebugSnapshotModel
import es.sebas1705.repositories.interfaces.IDebugSnapshotRepository
import javax.inject.Inject

class ReadDebugSnapshotsUseCase @Inject constructor(
    private val debugSnapshotRepository: IDebugSnapshotRepository
) {
    suspend operator fun invoke(limit: Int = DEFAULT_LIMIT): List<DebugSnapshotModel> =
        debugSnapshotRepository.readRecent(limit)
            .map {
                DebugSnapshotModel(
                    createdAtEpochMs = it.createdAtEpochMs,
                    selectedLanguage = it.selectedLanguage,
                    selectedLanguageWords = it.selectedLanguageWords,
                    duplicateWords = it.duplicateWords,
                    pureDuplicates = it.pureDuplicates,
                    invalidClueEntries = it.invalidClueEntries,
                    missingCategories = it.missingCategories,
                    validClueCoverage = it.validClueCoverage
                )
            }

    private companion object {
        const val DEFAULT_LIMIT = 25
    }
}

