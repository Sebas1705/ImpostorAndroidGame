package es.sebas1705.repositories.interfaces

import es.sebas1705.files.json.MyJson

/**
 * Repository interface to write and read data from the files
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
interface IFileRepository {

    /**
     * Reads a JSON file and returns its content as a [MyJson] object.
     *
     * @return [MyJson] object containing the data from the JSON file.
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    suspend fun readJsonFile(): MyJson

}