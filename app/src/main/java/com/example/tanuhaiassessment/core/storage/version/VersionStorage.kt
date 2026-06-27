package com.example.tanuhaiassessment.core.storage.version

interface VersionStorage {
    suspend fun saveVersion(
        modelId : String,
        version : String
    )

    suspend fun getVersion(
        modelId : String
    ): String
}