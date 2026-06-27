package com.example.tanuhaiassessment.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.tanuhaiassessment.core.storage.model.ModelStorage
import com.example.tanuhaiassessment.core.storage.model.ModelStorageImpl
import com.example.tanuhaiassessment.core.storage.version.VersionStorage
import com.example.tanuhaiassessment.core.storage.version.VersionStorageImpl
import com.example.tanuhaiassessment.core.storage.version.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {

        return context.dataStore
    }
}