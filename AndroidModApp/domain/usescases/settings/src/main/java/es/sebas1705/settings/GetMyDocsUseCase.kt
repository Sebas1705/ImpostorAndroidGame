package es.sebas1705.settings

import es.sebas1705.couchbase.documents.MyDoc
import es.sebas1705.repositories.interfaces.IMyDocRepository
import javax.inject.Inject

/**
 * Returns all MyDoc entries currently stored in Couchbase.
 */
class GetMyDocsUseCase @Inject constructor(
    private val myDocRepository: IMyDocRepository
) {
    suspend operator fun invoke(): List<MyDoc> =
        myDocRepository.getAllMyDocs()
}

