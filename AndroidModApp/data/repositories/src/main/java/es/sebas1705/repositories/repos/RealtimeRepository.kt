package es.sebas1705.repositories.repos

import es.sebas1705.realtime.datasources.MyJsonRealtimeDataSource
import es.sebas1705.repositories.interfaces.IRealtimeRepository
import javax.inject.Inject

/**
 * Realtime repository implementation
 *
 * @param myJsonRealtimeDataSource [MyJsonRealtimeDataSource]: Data source for realtime database operations
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class RealtimeRepository @Inject constructor(
    private val myJsonRealtimeDataSource: MyJsonRealtimeDataSource
) : IRealtimeRepository {

    //References:

    //Listeners:

    //Tasks:

    //Sets Listeners:

    //Removes Listeners:

}