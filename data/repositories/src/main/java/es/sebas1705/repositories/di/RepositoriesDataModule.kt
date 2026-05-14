package es.sebas1705.repositories.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.sebas1705.repositories.interfaces.IAnalyticsRepository
import es.sebas1705.repositories.interfaces.IAppSettingsRepository
import es.sebas1705.repositories.interfaces.IAuthenticationRepository
import es.sebas1705.repositories.interfaces.IDebugSnapshotRepository
import es.sebas1705.repositories.interfaces.IGameSettingsRepository
import es.sebas1705.repositories.interfaces.IOfflineRankingRepository
import es.sebas1705.repositories.interfaces.IOpendbRepository
import es.sebas1705.repositories.interfaces.IWordRepository
import es.sebas1705.repositories.repos.AnalyticsRepository
import es.sebas1705.repositories.repos.AppAppSettingsRepository
import es.sebas1705.repositories.repos.AuthenticationRepository
import es.sebas1705.repositories.repos.DebugSnapshotRepository
import es.sebas1705.repositories.repos.GameSettingsRepository
import es.sebas1705.repositories.repos.OfflineRankingRepository
import es.sebas1705.repositories.repos.OpendbRepository
import es.sebas1705.repositories.repos.WordRepository
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
     * Binds [IOpendbRepository] that is used to manage the open database
     *
     * @param impl [OpendbRepository]: Open Database Repository Implementation
     *
     * @return [IOpendbRepository]
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @Binds
    @Singleton
    abstract fun bindOpendbRepository(
        impl: OpendbRepository
    ): IOpendbRepository

    /**
     * Binds [IAppSettingsRepository] that is used to track events
     *
     * @param impl [AppAppSettingsRepository]: Settings Repository Implementation
     *
     * @return [IAppSettingsRepository]
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: AppAppSettingsRepository
    ): IAppSettingsRepository

    @Binds
    @Singleton
    abstract fun bindGameRepository(
        impl: GameSettingsRepository
    ): IGameSettingsRepository

    @Binds
    @Singleton
    abstract fun bindGameWordRepository(
        impl: WordRepository
    ): IWordRepository

    @Binds
    @Singleton
    abstract fun bindOfflineRankingRepository(
        impl: OfflineRankingRepository
    ): IOfflineRankingRepository

    @Binds
    @Singleton
    abstract fun bindDebugSnapshotRepository(
        impl: DebugSnapshotRepository
    ): IDebugSnapshotRepository

}