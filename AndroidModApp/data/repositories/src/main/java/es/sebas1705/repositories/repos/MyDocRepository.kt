package es.sebas1705.repositories.repos

import es.sebas1705.couchbase.datasources.MyDocCBDataSource
import es.sebas1705.couchbase.documents.MyDoc
import es.sebas1705.repositories.interfaces.IMyDocRepository
import javax.inject.Inject

class MyDocRepository @Inject constructor(
    private val myDocCBDataSource: MyDocCBDataSource
) : IMyDocRepository {

    override suspend fun getAllMyDocs(): List<MyDoc> =
        myDocCBDataSource.getAll()

    override suspend fun getMyDocById(id: Int): MyDoc? =
        myDocCBDataSource.get(id.toString())

    override suspend fun insertOrReplaceMyDoc(myDoc: MyDoc): Boolean =
        myDocCBDataSource.upsert(myDoc)

    override suspend fun deleteMyDoc(myDoc: MyDoc): Boolean =
        myDocCBDataSource.delete(myDoc.idUnique!!)

    override suspend fun cleanMyDocs(): Boolean =
        myDocCBDataSource.deleteAll()
}