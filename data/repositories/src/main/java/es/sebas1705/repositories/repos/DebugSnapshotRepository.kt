package es.sebas1705.repositories.repos

import es.sebas1705.couchbase.datasources.DebugSnapshotCBDataSource
import es.sebas1705.couchbase.documents.DebugSnapshotDoc
import es.sebas1705.repositories.interfaces.IDebugSnapshotRepository
import javax.inject.Inject

class DebugSnapshotRepository @Inject constructor(
    private val debugSnapshotCBDataSource: DebugSnapshotCBDataSource
) : IDebugSnapshotRepository {

    override suspend fun insert(snapshot: DebugSnapshotDoc): Boolean =
        debugSnapshotCBDataSource.upsert(snapshot)

    override suspend fun readRecent(limit: Int): List<DebugSnapshotDoc> =
        debugSnapshotCBDataSource
            .getAll()
            .sortedByDescending { it.createdAtEpochMs }
            .take(limit.coerceAtLeast(1))
}

