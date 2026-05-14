package es.sebas1705.game.debug

import es.sebas1705.couchbase.documents.DebugSnapshotDoc
import es.sebas1705.models.WordsDbStatsModel
import es.sebas1705.repositories.interfaces.IDebugSnapshotRepository
import java.util.UUID
import javax.inject.Inject

class SaveDebugSnapshotUseCase @Inject constructor(
    private val debugSnapshotRepository: IDebugSnapshotRepository
) {
    suspend operator fun invoke(stats: WordsDbStatsModel): Boolean {
        val snapshot = DebugSnapshotDoc(
            id = UUID.randomUUID().toString(),
            createdAtEpochMs = System.currentTimeMillis(),
            selectedLanguage = stats.selectedLanguage,
            selectedLanguageWords = stats.selectedLanguageWords,
            duplicateWords = stats.duplicateWordsInSelectedLanguage,
            pureDuplicates = stats.pureDuplicateEntriesInSelectedLanguage,
            invalidClueEntries = stats.invalidClueEntriesInSelectedLanguage,
            missingCategories = stats.missingCategoriesInSelectedLanguage,
            validClueCoverage = stats.validClueEntriesCoverageInSelectedLanguage
        )
        return debugSnapshotRepository.insert(snapshot)
    }
}

