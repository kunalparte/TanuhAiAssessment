package com.example.tanuhaiassessment.core.storage.model

import java.io.File

interface ModelStorage {

    fun getModelFile(
        modelId: String
    ): File

    fun modelExists(
        modelId: String
    ): Boolean
}