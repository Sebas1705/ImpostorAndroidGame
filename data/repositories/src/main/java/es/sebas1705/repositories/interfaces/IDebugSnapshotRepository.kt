package es.sebas1705.repositories.interfaces

import es.sebas1705.couchbase.documents.DebugSnapshotDoc

interface IDebugSnapshotRepository {
    suspend fun insert(snapshot: DebugSnapshotDoc): Boolean
    suspend fun readRecent(limit: Int): List<DebugSnapshotDoc>
}

