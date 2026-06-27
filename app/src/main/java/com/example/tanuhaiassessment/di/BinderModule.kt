package com.example.tanuhaiassessment.di

import com.example.tanuhaiassessment.core.checksum.ChecksumValidator
import com.example.tanuhaiassessment.core.checksum.ChecksumValidatorImpl
import com.example.tanuhaiassessment.core.inference.image.ImageClassifier
import com.example.tanuhaiassessment.core.inference.image.TFLiteImageClassifier
import com.example.tanuhaiassessment.core.inference.text.MobileBertClassifier
import com.example.tanuhaiassessment.core.inference.text.TextClassifier
import com.example.tanuhaiassessment.core.modelManagement.downloader.ModelDownloader
import com.example.tanuhaiassessment.core.modelManagement.downloader.ModelDownloaderImpl
import com.example.tanuhaiassessment.core.modelManagement.manaager.ModelManager
import com.example.tanuhaiassessment.core.modelManagement.manaager.ModelManagerImpl
import com.example.tanuhaiassessment.core.modelManagement.repository.ModelRepository
import com.example.tanuhaiassessment.core.modelManagement.repository.ModelRepositoryImpl
import com.example.tanuhaiassessment.core.scheduler.DownloadScheduler
import com.example.tanuhaiassessment.core.scheduler.DownloadSchedulerImpl
import com.example.tanuhaiassessment.core.storage.model.ModelStorage
import com.example.tanuhaiassessment.core.storage.model.ModelStorageImpl
import com.example.tanuhaiassessment.core.storage.version.VersionStorage
import com.example.tanuhaiassessment.core.storage.version.VersionStorageImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BinderModule {

    @Binds
    @Singleton
    abstract fun bindsModelRepository(
        modelRepositoryImpl: ModelRepositoryImpl
    ): ModelRepository


    @Binds
    @Singleton
    abstract fun bindsVersionStorage(
        versionStorageImpl: VersionStorageImpl
    ): VersionStorage


    @Binds
    @Singleton
    abstract fun bindsModelStorage(
        modelStorageImpl: ModelStorageImpl
    ): ModelStorage

    @Binds
    @Singleton
    abstract fun bindsModelManager(
        impl : ModelManagerImpl
    ): ModelManager

    @Binds
    @Singleton
    abstract fun providesModelDownloader(
        impl : ModelDownloaderImpl
    ): ModelDownloader

    @Binds
    @Singleton
    abstract fun bindCheckValidator(
        impl: ChecksumValidatorImpl
    ): ChecksumValidator

    @Binds
    abstract fun bindImageClassifier(
        impl: TFLiteImageClassifier
    ): ImageClassifier

    @Binds
    abstract fun bindsDownloadScheduler(
        impl: DownloadSchedulerImpl
    ): DownloadScheduler

    @Binds
    @Singleton
    abstract fun bindTextClassifier(
        impl : MobileBertClassifier
    ): TextClassifier
}

