package es.sebas1705.couchbase.di

import android.content.Context
import com.couchbase.lite.CouchbaseLite
import com.couchbase.lite.CouchbaseLiteException
import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseConfiguration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.sebas1705.couchbase.config.SettingsCB.COUCHBASE_DATABASE
import javax.inject.Singleton

/**
 * Module to provide
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@Module
@InstallIn(SingletonComponent::class)
class CouchbaseDataModule {

    /**
     * Provides [Database] that is used to manage the couchbase
     *
     * @param context [Context] to get the files directory
     *
     * @return [Database]
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @Provides
    @Singleton
    fun provideCouchbase(
        @ApplicationContext context: Context
    ): Database {
        CouchbaseLite.init(context);
        val config = DatabaseConfiguration()
        config.setDirectory(String.format("%s/%s", context.filesDir, COUCHBASE_DATABASE))
        try {
            return Database(COUCHBASE_DATABASE, config)
        } catch (e: CouchbaseLiteException) {
            e.printStackTrace()
            throw RuntimeException("Error creating database")
        }
    }
}