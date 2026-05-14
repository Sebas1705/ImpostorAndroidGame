package es.sebas1705.couchbase.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.sebas1705.couchbase.manager.CouchbaseManager
import es.sebas1705.couchbase.manager.ICouchbaseManager
import javax.inject.Singleton

/**
 * Module to bind
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class CouchbaseDataBindsModule {

    @Singleton
    @Binds
    internal abstract fun bindCouchbaseManager(
        couchbaseManager: CouchbaseManager
    ): ICouchbaseManager
}