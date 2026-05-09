package es.sebas1705.repositories.repos

import es.sebas1705.firestore.datasources.MyDocumentFirestoreDataSource
import es.sebas1705.repositories.interfaces.IFirestoreRepository
import javax.inject.Inject

/**
 * Implementation of IFirestoreRepository.
 *
 * @property myDocumentFirestoreDataSource [MyDocumentFirestoreDataSource]: Data source for managing documents in Firestore.
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class FirestoreRepository @Inject constructor(
    private val myDocumentFirestoreDataSource: MyDocumentFirestoreDataSource
) : IFirestoreRepository {


    //Listeners:


    //References:


    //Listeners:

}