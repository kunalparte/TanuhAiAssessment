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
import com.example.tanuhaiassessment.core.modelManagement.manaager.ModelManager
import com.example.tanuhaiassessment.core.usecase.GenerateInsightUseCase
import com.example.tanuhaiassessment.core.usecase.VerifyModelsReadyUseCase
import com.example.tanuhaiassessment.featureAssist.model.AssistantEvent
import com.example.tanuhaiassessment.featureAssist.model.AssistantState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AssistantVIewModel @Inject constructor(
    private val modelManager: ModelManager,
    private val imageClassifier: ImageClassifier,
    @ApplicationContext
    private val context: Context
): ViewModel(){

    private val _uiState = MutableStateFlow(AssistantState())
    val uiState = _uiState.asStateFlow()

    fun handleIntent( event: AssistantEvent){
        when (event) {

            is AssistantEvent.ImageSelected -> {
                onImageSelected(event.imageUri)
            }

            AssistantEvent.PickImageClicked -> {
                // No-op for now.
                // UI layer owns launching Photo Picker.
            }

            AssistantEvent.AnalyzeImageClicked -> {
                analyzeImage()
            }

            AssistantEvent.DownloadModelsClicked -> {
                downloadModels()
            }

            AssistantEvent.RetryClicked -> {
                analyzeImage()
            }

            AssistantEvent.ClearError -> {

                _uiState.update {

                    it.copy(
                        errorMessage = null
                    )
                }
            }
        }
    }

    private fun onImageSelected(uri: Uri) {

        _uiState.update {

            it.copy(
                selectedImageUri = uri,
                analysisResult = null,
                errorMessage = null
            )
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
                            errorMessage =
                                "Please select an image first"
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

                val classificationResult =
                    imageClassifier.classify(bitmap)

                _uiState.update {

                    it.copy(
                        isLoading = false,
                        analysisResult =
                            "${classificationResult.label} " +
                                    "(${
                                        String.format(
                                            "%.2f",
                                            classificationResult.confidence * 100
                                        )
                                    }%)"
                    )
                }

            } catch (exception: Exception) {

                _uiState.update {

                    it.copy(
                        isLoading = false,
                        errorMessage =
                            exception.message
                                ?: "Image analysis failed"
                    )
                }
            }
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
                        errorMessage =
                            exception.message
                                ?: "Failed to download models"
                    )
                }
            }
        }
    }

    /*private suspend fun uriToBitmap(
        uri: Uri
    ): Bitmap = withContext(Dispatchers.IO) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    context.contentResolver,
                    uri
                )
            )

        } else {

            MediaStore.Images.Media.getBitmap(
                context.contentResolver,
                uri
            )
        }
    }*/

    private suspend fun uriToBitmap(
        uri: Uri
    ): Bitmap = withContext(Dispatchers.IO) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            val source = ImageDecoder.createSource(
                context.contentResolver,
                uri
            )

            ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                decoder.isMutableRequired = true
            }

        } else {

            MediaStore.Images.Media.getBitmap(
                context.contentResolver,
                uri
            )
        }
    }
}