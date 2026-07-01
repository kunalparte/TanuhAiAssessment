package com.example.tanuhaiassessment.core.scheduler

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.impl.constraints.WorkConstraintsTracker
import androidx.work.workDataOf
import com.example.tanuhaiassessment.core.network.sto.ModelMetadataDto
import com.example.tanuhaiassessment.core.worker.ModelDownloadParams
import com.example.tanuhaiassessment.core.worker.ModelDownloaderWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DownloadSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DownloadScheduler {

    companion object{
        const val KEY_MODEL_ID = "KEY_MODEL_ID"
        const val KEY_VERSION = "KEY_VERSION"
        const val KEY_URL = "KEY_URL"

        const val KEY_CHECKSUM = "KEY_CHECKSUM"
    }

    override fun scheduleDownload(modelId: ModelDownloadParams) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val request = OneTimeWorkRequestBuilder<ModelDownloaderWorker>()
            .setConstraints(constraints)
            .setInputData(
                workDataOf(
                    KEY_MODEL_ID to modelId.modelId,
                    KEY_VERSION to modelId.version,
                    KEY_URL to modelId.url,
                    KEY_CHECKSUM to modelId.checksum
                )
            )
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                modelId.modelId,
                ExistingWorkPolicy.KEEP,
                request
            )

        WorkManager.getInstance(context)
            .getWorkInfoByIdLiveData(request.id)
            .observeForever { workInfo ->

                Log.d(
                    "WORK_STATE",
                    "State = ${workInfo?.state}"
                )

                Log.d(
                    "WORK_STATE",
                    "Info = $workInfo"
                )
            }

    }
}