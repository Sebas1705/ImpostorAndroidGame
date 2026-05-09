package es.sebas1705.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.MultiProcessDataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.sebas1705.datastore.SettingsPreferences
import es.sebas1705.datastore.serializers.SETTINGS_PREFERENCES_FILE_NAME
import es.sebas1705.datastore.serializers.SettingsSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

/**
 * Module to provide
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@Module
@InstallIn(SingletonComponent::class)
object DatastoreDataModule {

    /**
     * Provides all datastore
     */

    @Provides
    @Singleton
    fun provideSettingsPreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<SettingsPreferences> = MultiProcessDataStoreFactory.create(
        serializer = SettingsSerializer(),
        produceFile = { context.dataStoreFile(SETTINGS_PREFERENCES_FILE_NAME) },
        corruptionHandler = null,
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    )
}