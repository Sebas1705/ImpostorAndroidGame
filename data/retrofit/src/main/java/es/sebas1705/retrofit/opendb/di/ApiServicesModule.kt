package es.sebas1705.retrofit.opendb.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.sebas1705.retrofit.opendb.apis.OpendbApi
import es.sebas1705.retrofit.opendb.config.SettingsOpendb.TRIVIA_BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Module to provide Api services
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
@Module
@InstallIn(SingletonComponent::class)
class ApiServicesModule {

    /**
     * Provides [OpendbApi] that is used to get the questions
     *
     * @return [OpendbApi]
     *
     * @since 0.1.0
     * @author Sebas1705 01/03/2025
     */
    @Provides
    @Singleton
    fun provideTriviaApiService(okHttpClient: OkHttpClient): OpendbApi {
        return Retrofit.Builder()
            .baseUrl(TRIVIA_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpendbApi::class.java)
    }

}