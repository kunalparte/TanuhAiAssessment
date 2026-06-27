package com.example.tanuhaiassessment.di

import com.example.tanuhaiassessment.core.network.api.ManifestApi
import com.example.tanuhaiassessment.core.storage.model.ModelStorage
import com.example.tanuhaiassessment.core.storage.model.ModelStorageImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {

        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(
                "https://raw.githubusercontent.com/kunalparte/tanuh-ai-models/main/"
            )
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideManifestApi(
        retrofit: Retrofit
    ): ManifestApi {

        return retrofit.create(
            ManifestApi::class.java
        )
    }
}