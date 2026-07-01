package com.example.tanuhaiassessment.featureAssist.vm

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanuhaiassessment.core.inference.image.ImageClassifier
import com.example.tanuhaiassessment.core.inference.text.TextClassifier
import com.example.tanuhaiassessment.core.modelManagement.manaager.ModelManager
import com.example.tanuhaiassessment.featureAssist.model.AssistantEvent
import com.example.tanuhaiassessment.featureAssist.model.AssistantState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AssistantViewModel @Inject constructor(
    private val modelManager: ModelManager,
    private val imageClassifier: ImageClassifier,
    private val textClassifier: TextClassifier,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AssistantState()
    )

    val uiState: StateFlow<AssistantState> =
        _uiState.asStateFlow()

    fun handleIntent(
        event: AssistantEvent
    ) {
        when (event) {

            is AssistantEvent.ImageSelected -> {
                onImageSelected(event.imageUri)
            }

            AssistantEvent.AnalyzeImageClicked -> {
                analyzeImage()
            }

            AssistantEvent.DownloadModelsClicked -> {
                downloadModels()
            }

            AssistantEvent.ClearError -> {
                clearError()
            }

            AssistantEvent.PickImageClicked -> {
                // Handled by MainActivity because ActivityResultLauncher belongs to UI layer.
            }
        }
    }

    private fun onImageSelected(
        uri: Uri
    ) {
        _uiState.update {
            it.copy(
                selectedImageUri = uri,
                detectedObject = null,
                imageConfidence = null,
                generatedText = null,
                textCategory = null,
                textConfidence = null,
                errorMessage = null
            )
        }
    }

    private fun downloadModels() {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        errorMessage = null
                    )
                }

                modelManager.initialize()

                _uiState.update {
                    it.copy(
                        isLoading = false
                    )
                }

            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message
                            ?: "Failed to download models"
                    )
                }
            }
        }
    }

    private fun analyzeImage() {
        viewModelScope.launch {
            try {
                val imageUri =
                    uiState.value.selectedImageUri

                if (imageUri == null) {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Please select an image first"
                        )
                    }
                    return@launch
                }

                _uiState.update {
                    it.copy(
                        isLoading = true,
                        errorMessage = null
                    )
                }

                val bitmap =
                    uriToBitmap(imageUri)

                val imageResult =
                    imageClassifier.classify(bitmap)

                val generatedText =
                    buildGeneratedText(imageResult.label)

                val textResult =
                    textClassifier.classify(generatedText)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        detectedObject = imageResult.label,
                        imageConfidence = imageResult.confidence,
                        generatedText = generatedText,
                        textCategory = textResult.category,
                        textConfidence = textResult.confidence,
                        errorMessage = null
                    )
                }

            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message
                            ?: "Analysis failed"
                    )
                }
            }
        }
    }

    private suspend fun uriToBitmap(
        uri: Uri
    ): Bitmap = withContext(Dispatchers.IO) {

        val bitmap =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                val source =
                    ImageDecoder.createSource(
                        context.contentResolver,
                        uri
                    )

                ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                    decoder.allocator =
                        ImageDecoder.ALLOCATOR_SOFTWARE

                    decoder.isMutableRequired = true
                }

            } else {
                MediaStore.Images.Media.getBitmap(
                    context.contentResolver,
                    uri
                )
            }

        bitmap.copy(
            Bitmap.Config.ARGB_8888,
            true
        )
    }

    private fun buildGeneratedText(
        detectedLabel: String
    ): String {
        return "The uploaded image appears to contain $detectedLabel. This note was generated from an on-device image classification result."
    }

    private fun clearError() {
        _uiState.update {
            it.copy(
                errorMessage = null
            )
        }
    }
}