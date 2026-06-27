package com.example.tanuhaiassessment.core.inference.text

interface TextClassifier {
    suspend fun classify(
        label: String
    ): TextClassificationResult
}