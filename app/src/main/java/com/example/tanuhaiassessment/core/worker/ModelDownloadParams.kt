package com.example.tanuhaiassessment.core.worker

data class ModelDownloadParams(
    val modelId: String,
    val version: String,
    val url: String,
    val checksum: String
)
