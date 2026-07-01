package com.example.tanuhaiassessment.featureAssist.model

import android.net.Uri
import com.example.tanuhaiassessment.core.inference.image.ImageClassificationResult

data class AssistantState(
    val selectedImageUri: Uri? = null,

    val isLoading: Boolean = false,

    val detectedObject: String? = null,
    val imageConfidence: Float? = null,

    val generatedText: String? = null,

    val textCategory: String? = null,
    val textConfidence: Float? = null,

    val errorMessage: String? = null

)