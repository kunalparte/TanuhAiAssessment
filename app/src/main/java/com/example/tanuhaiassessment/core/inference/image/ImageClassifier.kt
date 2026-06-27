package com.example.tanuhaiassessment.core.inference.image

import android.graphics.Bitmap

interface ImageClassifier {

    suspend fun classify(
        image: Bitmap
    ): ImageClassificationResult
}