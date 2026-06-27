package com.example.tanuhaiassessment.core.inference.text

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FallbackTextClassifier @Inject constructor()
    : TextClassifier{
    override suspend fun classify(label: String): TextClassificationResult {
        return TextClassificationResult(
            "Unknown",
            0f
        )
    }
}