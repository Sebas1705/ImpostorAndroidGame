package es.sebas1705.app.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module to provide firebase dependencies
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    /**
     * Provides [FirebaseAnalytics] that is used to track events
     *
     * @param context [Context]: Application context to initialize Firebase Analytics
     *
     * @return [FirebaseAnalytics]
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @Provides
    @Singleton
    fun provideFirebaseAnalytics(
        @ApplicationContext context: Context
    ): FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    /*
    /**
     * Provides [FirebaseAuth] that is used to manage the authentication
     *
     * @return [FirebaseAuth]
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Provides [FirebaseDatabase] that is used to manage the realtime database
     *
     * @return [FirebaseDatabase]
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @Provides
    @Singleton
    fun provideFirebaseRealtime(): FirebaseDatabase = FirebaseDatabase.getInstance()

    /**
     * Provides [FirebaseFirestore] that is used to manage the firestore database
     *
     * @return [FirebaseFirestore]
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @Provides
    @Singleton
    fun provideFirestoreFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
    */
}