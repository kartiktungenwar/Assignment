package com.bookxpert.assignment.hilt

import android.content.Context
import com.bookxpert.assignment.database.UserDao
import com.bookxpert.assignment.network.ApiService
import com.bookxpert.assignment.preferencesDataStore.PreferencesDataStore
import com.bookxpert.assignment.service.repo.MainRepository
import com.bookxpert.assignment.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Poonamchand Sahu 11 Nov 2022
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * This method used for provide ApiService instance to Hilt DI
     */
    @Provides
    fun provideApi(appSharedPref: PreferencesDataStore, @ApplicationContext context: Context): ApiService = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(provideOkHttpClient(appSharedPref))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    /**
     * This method used for provide MainRepository instance to Hilt DI
     */
    @Provides
    fun provideRepository(dataStore: PreferencesDataStore, retrofitApi: ApiService,userDao: UserDao): MainRepository =
        MainRepository(dataStore,retrofitApi,userDao)

    /**
     * This method used for provide OkHttpClient instance to Hilt DI
     */
    @Provides
    fun provideOkHttpClient(appSharedPref: PreferencesDataStore): OkHttpClient {
        return provideOkHttpClientInternal(appSharedPref)
    }

    private fun provideOkHttpClientInternal(appSharedPref: PreferencesDataStore): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            val interceptor = HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }
        okHttpBuilder.addInterceptor(interceptor)
        okHttpBuilder.addInterceptor { chain ->
            val request = chain.request().newBuilder()
            request.addHeader("Content-Type", "application/json")
            chain.proceed(request.build())
        }
        val okHttpClient = okHttpBuilder.build()
        okHttpClient.dispatcher.maxRequests = 6
        return okHttpClient
    }

}