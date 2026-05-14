package es.sebas1705.room.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.sebas1705.room.Database
import es.sebas1705.room.config.SettingsDB
import javax.inject.Singleton

/**
 * Module to provide
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@Module
@InstallIn(SingletonComponent::class)
class RoomDataModule {

    /**
     * Provides [Database] that is used to manage the local database
     *
     * @param context [Context]: Application
     *
     * @return [Database]
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @Provides
    @Singleton
    fun provideLocalDatabase(
        @ApplicationContext context: Context
    ): Database = Room.databaseBuilder(
        context,
        Database::class.java,
        SettingsDB.DATABASE_NAME
    ).build()

}