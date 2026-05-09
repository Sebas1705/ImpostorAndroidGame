package es.sebas1705.repositories.interfaces

import es.sebas1705.room.entities.MyEntity


/**
 * Interface to represent the repository of the database
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
interface IDatabaseRepository {

    //Selects
    /**
     * Check if the entity exists
     *
     * @param name [String]: Name
     *
     * @return [Boolean]: If the entity exists
     *
     * @since 0.1.0
     */
    suspend fun containsMyEntity(name: String): Boolean

    /**
     * Get the entity with the name
     *
     * @param name [String]: Name
     *
     * @return [MyEntity]: Entity with the name
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    suspend fun getMyEntity(name: String): MyEntity

    //Inserts
    /**
     * Insert or replace the entity
     *
     * @param myEntity [MyEntity]: Entity to insert or replace
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    suspend fun insertOrReplace(myEntity: MyEntity)

    //Updates


    //Deletes
    /**
     * Delete the entity with the name
     *
     * @param name [String]: Name
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    suspend fun deleteMyEntity(name: String)

}