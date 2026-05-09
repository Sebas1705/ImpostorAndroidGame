package es.sebas1705.repositories.repos

import es.sebas1705.files.datasource.MyJsonFileDataSource
import es.sebas1705.files.json.MyJson
import es.sebas1705.repositories.interfaces.IFileRepository
import javax.inject.Inject

/**
 * Class to represent the repository of the files
 *
 * @property myJsonFileDataSource [MyJsonFileDataSource]: Data source for reading JSON files
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
class FileRepository @Inject constructor(
    private val myJsonFileDataSource: MyJsonFileDataSource
) : IFileRepository {

    override suspend fun readJsonFile(): MyJson =
        myJsonFileDataSource.readJsonFile()

}