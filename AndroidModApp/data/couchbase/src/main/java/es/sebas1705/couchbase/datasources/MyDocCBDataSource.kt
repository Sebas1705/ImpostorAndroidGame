package es.sebas1705.couchbase.datasources

import es.sebas1705.couchbase.documents.MyDoc
import es.sebas1705.couchbase.manager.ICouchbaseManager
import javax.inject.Inject

/**
 * Data source for managing [MyDoc] documents in Couchbase.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class MyDocCBDataSource @Inject constructor(
    couchbaseManager: ICouchbaseManager
) : CBDataSource<MyDoc>(couchbaseManager, MyDoc::class)