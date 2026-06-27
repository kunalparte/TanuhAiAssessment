package com.example.tanuhaiassessment.core.scheduler

import com.example.tanuhaiassessment.core.worker.ModelDownloadParams

interface DownloadScheduler {

    fun scheduleDownload(
        modelId : ModelDownloadParams
    )
}