package com.example.tanuhaiassessment.core.modelManagement.manaager

import com.example.tanuhaiassessment.core.modelManagement.downloader.ModelDownloader
import com.example.tanuhaiassessment.core.modelManagement.model.ModelMetadata
import com.example.tanuhaiassessment.core.modelManagement.repository.ModelRepository
import com.example.tanuhaiassessment.core.network.api.ManifestApi
import com.example.tanuhaiassessment.core.scheduler.DownloadScheduler
import com.example.tanuhaiassessment.core.storage.model.ModelStorage
import com.example.tanuhaiassessment.core.storage.version.VersionStorage
import com.example.tanuhaiassessment.core.worker.ModelDownloadParams
import javax.inject.Inject

class ModelManagerImpl @Inject constructor(
    private val modelRepository: ModelRepository,
    private val versionStorage: VersionStorage,
    private val scheduler: DownloadScheduler
): ModelManager {

    override suspend fun initialize() {
        val serverModels =
            modelRepository.fetchManifest()

        serverModels.forEach { serverModel ->
            processModel(serverModel)
        }
    }

    private suspend fun processModel(metaData : ModelMetadata){
        val versionStored = versionStorage.getVersion(
            metaData.id
        )

        val shouldDownload =
            versionStored == null ||
                    versionStored != metaData.version

        if (shouldDownload){
            scheduler.scheduleDownload(
                ModelDownloadParams(
                    modelId = metaData.id,
                    version = metaData.version,
                    url = metaData.url,
                    checksum = metaData.checksum
                )
            )
        }
    }
}