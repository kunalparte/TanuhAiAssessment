package com.example.tanuhaiassessment.core.modelManagement.downloader

import java.io.File

interface ModelDownloader {

    suspend fun download(
        url: String,
        destination: File
    )
}