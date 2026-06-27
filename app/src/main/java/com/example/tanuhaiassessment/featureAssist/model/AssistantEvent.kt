package com.example.tanuhaiassessment.featureAssist.model

import android.net.Uri

sealed interface AssistantEvent {

    data class ImageSelected(
        val imageUri: Uri
    ) : AssistantEvent

    data object PickImageClicked : AssistantEvent

    data object AnalyzeImageClicked : AssistantEvent

    data object DownloadModelsClicked : AssistantEvent

    data object RetryClicked : AssistantEvent

    data object ClearError : AssistantEvent
}