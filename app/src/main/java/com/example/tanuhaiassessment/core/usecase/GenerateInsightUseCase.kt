package com.example.tanuhaiassessment.core.usecase

import android.graphics.Bitmap
import com.example.tanuhaiassessment.core.inference.image.ImageClassifier
import com.example.tanuhaiassessment.core.inference.text.TextClassifier
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenerateInsightUseCase @Inject constructor(
    private val imageClassifier: ImageClassifier,
    private val textClassifier: TextClassifier
){
    suspend operator fun invoke(
        bitmap: Bitmap
    ): String{
        val imageResult =
            imageClassifier.classify(bitmap)

        val textResult =
            textClassifier.classify(
                imageResult.label
            )

        return """
        Object: ${imageResult.label}
        Confidence: ${imageResult.confidence}
        Category: ${textResult.category}
    """.trimIndent()
    }
}