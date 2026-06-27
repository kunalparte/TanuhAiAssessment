package com.example.tanuhaiassessment.core.modelManagement.downloader

import android.util.Log
import com.example.tanuhaiassessment.core.modelManagement.repository.ModelRepository
import com.example.tanuhaiassessment.core.storage.version.VersionStorage
import com.example.tanuhaiassessment.core.storage.model.ModelStorage
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import javax.inject.Inject

class ModelDownloaderImpl @Inject constructor(
    private val okHttpClient: OkHttpClient
) : ModelDownloader{

    override suspend fun download(url: String, destination: File) {
        Log.d(
            "MODEL_DOWNLOAD",
            "Starting download from: $url"
        )

        val request = Request.Builder()
            .url(url)
            .build()

        val response = okHttpClient
            .newCall(request)
            .execute()

        if (!response.isSuccessful) {
            throw IllegalStateException(
                "Download failed: ${response.code}"
            )
        }

        val body = response.body
            ?: throw IllegalStateException(
                "Response body is null"
            )

        destination.outputStream().use { outputStream ->

            body.byteStream().use { inputStream ->

                inputStream.copyTo(
                    outputStream
                )
            }
        }
        Log.d(
            "MODEL_DOWNLOAD",
            """
        File downloaded successfully
        Path = ${destination.absolutePath}
        Exists = ${destination.exists()}
        Size = ${destination.length()} bytes
        """.trimIndent()
        )
    }

}