package es.sebas1705.couchbase.datasources

import es.sebas1705.couchbase.datasources.abstracts.CBDataSource
import es.sebas1705.couchbase.documents.DebugSnapshotDoc
import es.sebas1705.couchbase.manager.ICouchbaseManager
import javax.inject.Inject

class DebugSnapshotCBDataSource @Inject constructor(
    couchbaseManager: ICouchbaseManager
) : CBDataSource<DebugSnapshotDoc>(couchbaseManager, DebugSnapshotDoc::class)

