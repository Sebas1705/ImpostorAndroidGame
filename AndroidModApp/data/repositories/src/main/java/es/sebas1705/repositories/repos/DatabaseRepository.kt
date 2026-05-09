package es.sebas1705.repositories.repos

import es.sebas1705.analytics.datasources.LogEventDataSource
import es.sebas1705.common.managers.ClassLogData
import es.sebas1705.repositories.interfaces.IDatabaseRepository
import es.sebas1705.room.Database
import es.sebas1705.room.entities.MyEntity
import javax.inject.Inject

/**
 * Class to represent the repository of the database
 *
 * @property roomDatabase [Database]: Database
 * @property logEventDataSource [LogEventDataSource]: Data source for logging events
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class DatabaseRepository @Inject constructor(
    private val roomDatabase: Database,
    private val logEventDataSource: LogEventDataSource
) : IDatabaseRepository, ClassLogData() {

    //Selects
    override suspend fun containsMyEntity(name: String): Boolean =
        roomDatabase.myDao().contains(name) > 0

    override suspend fun getMyEntity(name: String): MyEntity =
        roomDatabase.myDao().getMyEntity(name)


    //Inserts
    override suspend fun insertOrReplace(myEntity: MyEntity) {
        roomDatabase.myDao().insertOrReplace(myEntity)
    }

    //Updates


    //Deletes
    override suspend fun deleteMyEntity(name: String) {
        roomDatabase.myDao().delete(name)
    }

}