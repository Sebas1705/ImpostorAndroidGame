package es.sebas1705.repositories.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.sebas1705.repositories.interfaces.IAnalyticsRepository
import es.sebas1705.repositories.interfaces.IAuthenticationRepository
import es.sebas1705.repositories.interfaces.IDatabaseRepository
import es.sebas1705.repositories.interfaces.IFileRepository
import es.sebas1705.repositories.interfaces.IFirestoreRepository
import es.sebas1705.repositories.interfaces.IGameRepository
import es.sebas1705.repositories.interfaces.IGameWordRepository
import es.sebas1705.repositories.interfaces.IMyDocRepository
import es.sebas1705.repositories.interfaces.IOfflineRankingRepository
import es.sebas1705.repositories.interfaces.IOpendbRepository
import es.sebas1705.repositories.interfaces.IRealtimeRepository
import es.sebas1705.repositories.interfaces.ISettingsRepository
import es.sebas1705.repositories.repos.AnalyticsRepository
import es.sebas1705.repositories.repos.AuthenticationRepository
import es.sebas1705.repositories.repos.DatabaseRepository
import es.sebas1705.repositories.repos.FileRepository
import es.sebas1705.repositories.repos.FirestoreRepository
import es.sebas1705.repositories.repos.GameRepository
import es.sebas1705.repositories.repos.GameWordRepository
import es.sebas1705.repositories.repos.MyDocRepository
import es.sebas1705.repositories.repos.OfflineRankingRepository
import es.sebas1705.repositories.repos.OpendbRepository
import es.sebas1705.repositories.repos.RealtimeRepository
import es.sebas1705.repositories.repos.SettingsRepository
import javax.inject.Singleton

/**
 * Module to provide repositories in the data layer.
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@Module
@InstallIn(SingletonComponent::class)
@Suppress("TooManyFunctions")
abstract class RepositoriesDataModule {

    /**
     * Provides [IAnalyticsRepository] that is used to track events
     *
     * @param impl [AnalyticsRepository]: Analytics Repository Implementation
     *
     * @return [IAnalyticsRepository]
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @Binds
    @Singleton
    abstract fun bindAnalyticsRepository(
        impl: AnalyticsRepository
    ): IAnalyticsRepository

    /**
     * Binds [IAuthenticationRepository] that is used to manage the authentication
     *
     * @param impl [AuthenticationRepository]: Authentication Repository Implementation
     *
     * @return [IAuthenticationRepository]
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthenticationRepository
    ): IAuthenticationRepository

    /**
     * Binds [IDatabaseRepository] that is used to manage the local database
     *
     * @param impl [DatabaseRepository]: Implementation of the repository
     *
     * @return [IDatabaseRepository]
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @Binds
    @Singleton
    abstract fun bindDatabaseRepository(
        impl: DatabaseRepository
    ): IDatabaseRepository

    /**
     * Binds [IFileRepository] that is used to manage the files
     *
     * @param impl [FileRepository]: Implementation of the [IFileRepository]
     *
     * @return [IFileRepository]
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @Binds
    @Singleton
    abstract fun bindFileRepository(
        impl: FileRepository
    ): IFileRepository

    /**
     * Binds [IFirestoreRepository] that is used to manage the firestore database
     *
     * @param impl [FirestoreRepository]: Implementation of the [IFirestoreRepository]
     *
     * @return [IFirestoreRepository]
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @Binds
    @Singleton
    abstract fun bindFirestoreRepository(
        impl: FirestoreRepository
    ): IFirestoreRepository

    /**
     * Binds [IRealtimeRepository] that is used to manage the realtime database
     *
     * @param impl [RealtimeRepository]: Implementation of the [IRealtimeRepository]
     *
     * @return [IRealtimeRepository]
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @Binds
    @Singleton
    abstract fun bindRealtimeRepository(
        impl: RealtimeRepository
    ): IRealtimeRepository

    /**
     * Binds [IOpendbRepository] that is used to get the questions
     *
     * @param impl [OpendbRepository]: Implementation of the repository
     *
     * @return [IOpendbRepository]
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @Binds
    @Singleton
    abstract fun bindMyDocRepository(
        impl: MyDocRepository
    ): IMyDocRepository

    @Binds
    @Singleton
    abstract fun bindOpendbRepository(
        impl: OpendbRepository
    ): IOpendbRepository

    /**
     * Binds [ISettingsRepository] that is used to track events
     *
     * @param impl [SettingsRepository]: Settings Repository Implementation
     *
     * @return [ISettingsRepository]
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepository
    ): ISettingsRepository

    @Binds
    @Singleton
    abstract fun bindGameRepository(
        impl: GameRepository
    ): IGameRepository

    @Binds
    @Singleton
    abstract fun bindGameWordRepository(
        impl: GameWordRepository
    ): IGameWordRepository

    @Binds
    @Singleton
    abstract fun bindOfflineRankingRepository(
        impl: OfflineRankingRepository
    ): IOfflineRankingRepository

}