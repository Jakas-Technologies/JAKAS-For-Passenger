package com.miftah.jakasforpassenger.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.miftah.jakasforpassenger.BuildConfig
import com.miftah.jakasforpassenger.core.data.source.AppRepository
import com.miftah.jakasforpassenger.core.data.source.preference.UserPref
import com.miftah.jakasforpassenger.core.data.source.preference.UserPreferenceImpl
import com.miftah.jakasforpassenger.core.data.source.preference.dataStore
import com.miftah.jakasforpassenger.core.data.source.remote.retrofit.ApiHelper
import com.miftah.jakasforpassenger.core.data.source.remote.retrofit.ApiService
import com.miftah.jakasforpassenger.core.data.source.remote.retrofit.ApiServiceImpl
import com.miftah.jakasforpassenger.core.data.source.remote.retrofit.MidtransApiHelper
import com.miftah.jakasforpassenger.core.data.source.remote.retrofit.MidtransApiService
import com.miftah.jakasforpassenger.core.data.source.remote.retrofit.MidtransApiServiceImpl
import com.miftah.jakasforpassenger.core.provider.DefaultLocationClient
import com.miftah.jakasforpassenger.core.provider.LocationClient
import com.miftah.jakasforpassenger.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRepository(
        apiService: ApiService,
        apiMidtransService: MidtransApiService
    ): AppRepository = AppRepository(apiService, apiMidtransService)

    @Provides
    @Named("URL")
    fun provideBaseUrl(): String = Constants.BASE_URL

    @Provides
    @Singleton
    fun provideLogging(): HttpLoggingInterceptor =
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

    @Provides
    @Singleton
    fun provideAuth(userPref : UserPref): Interceptor = Interceptor { chain ->
        val req = chain.request()

        val token = runBlocking {
            userPref.getSession().first().token
        }

        val requestHeaders = if (token.isNotBlank()) {
            req.newBuilder()
                .addHeader("auth", "Bearer $token")
                .build()
        } else {
            req
        }
        chain.proceed(requestHeaders)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(loggingInterceptor: HttpLoggingInterceptor, interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(interceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(@Named("URL") url: String, client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun bindApiServiceImpl(apiServiceImpl: ApiServiceImpl): ApiHelper = apiServiceImpl

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(@ApplicationContext app: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(app)

    @Provides
    @Singleton
    fun providesDefaultLocationClient(
        fusedLocationProviderClient: FusedLocationProviderClient,
        @ApplicationContext app: Context
    ): LocationClient =
        DefaultLocationClient(app, fusedLocationProviderClient)

    @Provides
    @Named("MIDTRANS_URL")
    fun provideMidtransUrl(): String = Constants.MIDTRANS_URL

    @Provides
    @Singleton
    @Named("MIDTRANS_RETROFIT")
    fun provideMidtransRetrofit(
        @Named("MIDTRANS_URL") url: String,
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Provides
    @Singleton
    fun provideMidtransApiService(@Named("MIDTRANS_RETROFIT") retrofit: Retrofit): MidtransApiService =
        retrofit.create(MidtransApiService::class.java)

    @Provides
    @Singleton
    fun bindMidtransApiServiceImpl(midtransApiServiceImpl: MidtransApiServiceImpl): MidtransApiHelper =
        midtransApiServiceImpl

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext app: Context): DataStore<Preferences> = app.dataStore

    @Singleton
    @Provides
    fun proveideUserPreference(userPreference: UserPreferenceImpl): UserPref = userPreference
}