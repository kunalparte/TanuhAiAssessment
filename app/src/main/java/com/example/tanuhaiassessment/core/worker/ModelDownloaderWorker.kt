package com.example.tanuhaiassessment.core.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tanuhaiassessment.core.checksum.ChecksumValidator
import com.example.tanuhaiassessment.core.modelManagement.downloader.ModelDownloader
import com.example.tanuhaiassessment.core.modelManagement.manaager.ModelManager
import com.example.tanuhaiassessment.core.modelManagement.repository.ModelRepository
import com.example.tanuhaiassessment.core.scheduler.DownloadSchedulerImpl.Companion.KEY_CHECKSUM
import com.example.tanuhaiassessment.core.scheduler.DownloadSchedulerImpl.Companion.KEY_MODEL_ID
import com.example.tanuhaiassessment.core.scheduler.DownloadSchedulerImpl.Companion.KEY_URL
import com.example.tanuhaiassessment.core.scheduler.DownloadSchedulerImpl.Companion.KEY_VERSION
import com.example.tanuhaiassessment.core.storage.model.ModelStorage
import com.example.tanuhaiassessment.core.storage.version.VersionStorage
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class ModelDownloaderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val downloader: ModelDownloader,
    private val versionStorage: VersionStorage,
    private val modelStorage: ModelStorage,
    private val  checksumValidator: ChecksumValidator
) : CoroutineWorker(context, params){

    override suspend fun doWork(): Result {

        return try {

            val modelId = inputData.getString(KEY_MODEL_ID)
            val version = inputData.getString(KEY_VERSION)
            val url = inputData.getString(KEY_URL)
            val checksum = inputData.getString(KEY_CHECKSUM)

            if (
                modelId == null ||
                version == null ||
                url == null ||
                checksum == null
            ) {
                return Result.failure()
            }

            val file = modelStorage.getModelFile(modelId)

            // Step 1: Download the model
            downloader.download(
                url = url,
                destination = file
            )

            // Step 2: Validate the downloaded file
            val isValid = checksumValidator.validate(
                file = file,
                expectedChecksum = checksum
            )

            if (!isValid) {
                file.delete()
                return Result.failure()
            }

            // Step 3: Save the version
            versionStorage.saveVersion(
                modelId,
                version
            )

            Result.success()

        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }

    }
}