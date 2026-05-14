package es.sebas1705.couchbase.di

import android.content.Context
import com.couchbase.lite.CouchbaseLite
import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseConfiguration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.sebas1705.common.utlis.extensions.types.logE
import es.sebas1705.common.utlis.extensions.types.logI
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
    ): Database? {
        "CouchbaseDataModule".logI(
            "provideCouchbase start db=$COUCHBASE_DATABASE dir=${context.filesDir}/$COUCHBASE_DATABASE"
        )
        val database = try {
            CouchbaseLite.init(context)
            val config = DatabaseConfiguration()
            config.setDirectory("${context.filesDir}/$COUCHBASE_DATABASE")
            Database(COUCHBASE_DATABASE, config)
        } catch (e: UnsatisfiedLinkError) {
            "CouchbaseDataModule".logE(
                "provideCouchbase failed reason=native_library_load_error db=$COUCHBASE_DATABASE",
                e
            )
            null
        } catch (e: Throwable) {
            "CouchbaseDataModule".logE(
                "provideCouchbase failed reason=unexpected_error db=$COUCHBASE_DATABASE",
                e
            )
            null
        }
        "CouchbaseDataModule".logI("provideCouchbase done available=${database != null}")
        return database
    }
}