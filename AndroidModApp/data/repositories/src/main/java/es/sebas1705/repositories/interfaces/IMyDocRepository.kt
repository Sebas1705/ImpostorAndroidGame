package es.sebas1705.repositories.interfaces

import es.sebas1705.couchbase.documents.MyDoc

/**
 * Repository interface to write and read data from Couchbase
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
interface IMyDocRepository {

    //Selects:
    /**
     * Get all MyDocs from the database
     *
     * @return [List]<[MyDoc]> A list of all MyDocs in the database
     */
    suspend fun getAllMyDocs(): List<MyDoc>

    /**
     * Get a MyDoc by its ID
     *
     * @param id [Int] The ID of the MyDoc to retrieve
     * @return [MyDoc]? The MyDoc with the specified ID, or null if not found
     */
    suspend fun getMyDocById(id: Int): MyDoc?

    //Inserts:
    /**
     * Insert a MyDoc into the database
     *
     * @param myDoc [MyDoc] The MyDoc to insert
     * @return [Boolean] True if the insertion was successful, false otherwise
     */
    suspend fun insertOrReplaceMyDoc(myDoc: MyDoc): Boolean

    //Deletes:
    /**
     * Delete a MyDoc from the database
     *
     * @param myDoc [MyDoc] The MyDoc to delete
     * @return [Boolean] True if the deletion was successful, false otherwise
     */
    suspend fun deleteMyDoc(myDoc: MyDoc): Boolean

    /**
     * Delete all MyDocs from the database
     *
     * @return [Boolean] True if the deletion was successful, false otherwise
     */
    suspend fun cleanMyDocs(): Boolean

}