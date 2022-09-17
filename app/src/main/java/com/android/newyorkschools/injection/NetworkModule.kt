package com.android.newyorkschools.injection

import com.android.newyorkschools.BuildConfig
import com.android.newyorkschools.cloud.api.SchoolApi
import com.android.newyorkschools.cloud.api.SchoolEndpoints
import com.android.newyorkschools.cloud.service.SchoolWebService
import com.android.newyorkschools.util.LocalDispatchers
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return getGson()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideSchoolWebService(
        dispatchers: LocalDispatchers,
        retrofit: Retrofit
    ): SchoolWebService {
        val schoolsApi = retrofit.create(SchoolApi::class.java)
        return SchoolWebService(dispatchers, schoolsApi, SchoolEndpoints())
    }

    companion object {
        fun getGson(): Gson {
            return GsonBuilder().create()
        }
    }
}