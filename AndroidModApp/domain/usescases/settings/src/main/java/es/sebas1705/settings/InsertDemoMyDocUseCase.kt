package es.sebas1705.settings

import es.sebas1705.couchbase.documents.MyDoc
import es.sebas1705.repositories.interfaces.IMyDocRepository
import javax.inject.Inject

/**
 * Inserts a sample MyDoc row to simplify Couchbase runtime inspection.
 */
class InsertDemoMyDocUseCase @Inject constructor(
    private val myDocRepository: IMyDocRepository
) {
    suspend operator fun invoke(): Boolean {
        val id = (System.currentTimeMillis() % 1_000_000_000L).toInt()
        val myDoc = MyDoc(
            id = id,
            name = "Debug doc $id"
        )
        return myDocRepository.insertOrReplaceMyDoc(myDoc)
    }
}

