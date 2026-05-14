package es.sebas1705.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import es.sebas1705.room.config.SettingsDB

/**
 * Data class to represent the user data and use as entity in the database
 *
 * @property name [String]: Name
 * @property age [Int]: Age
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@Entity(tableName = SettingsDB.TABLE_NAME)
data class MyEntity(
    @PrimaryKey val name: String,
    val age: Int,
)