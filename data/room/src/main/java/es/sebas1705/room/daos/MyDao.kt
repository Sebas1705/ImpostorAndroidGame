package es.sebas1705.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.sebas1705.room.entities.MyEntity

/**
 * DAO to manage the database
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@Dao
interface MyDao{

    /**
     * Check if the entity exists
     *
     * @param name [String]: Name
     *
     * @return [Int]: Number of entities with the name
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @Query("SELECT COUNT(*) FROM `Table` WHERE name = :name")
    suspend fun contains(name: String): Int

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
    @Query("SELECT * FROM `Table` WHERE name = :name")
    suspend fun getMyEntity(name: String): MyEntity

    /**
     * Insert or replace the entity
     *
     * @param myEntity [MyEntity]: Entity to insert or replace
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(myEntity: MyEntity)

    /**
     * Delete the entity with the name
     *
     * @param name [String]: Name
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @Query("DELETE FROM `Table` WHERE name = :name")
    suspend fun delete(name: String)

}