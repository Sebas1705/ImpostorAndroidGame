package es.sebas1705.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import es.sebas1705.room.daos.MyDao
import es.sebas1705.room.entities.MyEntity
import es.sebas1705.room.typeconverter.Converter

/**
 * Local database
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@Database(
    entities = [
        MyEntity::class
    ],
    version = 1
)
@TypeConverters(Converter::class)
abstract class Database : RoomDatabase() {

    //DAOs:
    abstract fun myDao(): MyDao

}