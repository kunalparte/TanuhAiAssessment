package com.example.tanuhaiassessment.core.storage.model

import android.content.Context
import com.example.tanuhaiassessment.core.storage.model.ModelStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class ModelStorageImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ModelStorage{

    override fun getModelFile(modelId: String): File {
        val modelDirectory =
            File(
                context.filesDir,
                "models"
            )

        if (!modelDirectory.exists()) {
            modelDirectory.mkdirs()
        }

        return File(
            modelDirectory,
            "$modelId.tflite"
        )
    }

    override fun modelExists(modelId: String): Boolean {
        return getModelFile(
            modelId
        ).exists()
    }


}