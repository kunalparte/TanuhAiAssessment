package com.example.tanuhaiassessment.featureAssist.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.tanuhaiassessment.featureAssist.model.AssistantEvent
import com.example.tanuhaiassessment.featureAssist.model.AssistantState

@Composable
fun AssistantScreen(
    uiState: AssistantState,
    onEvent: (AssistantEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Tanuh AI Assessment",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onEvent(AssistantEvent.PickImageClicked)
            }
        ) {
            Text(text = "Select Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        SelectedImageSection(
            imageUri = uiState.selectedImageUri
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            enabled = uiState.selectedImageUri != null && !uiState.isLoading,
            onClick = {
                onEvent(AssistantEvent.AnalyzeImageClicked)
            }
        ) {
            Text(text = "Analyze Image")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (uiState.detectedObject != null) {
            ResultCard(uiState = uiState)
        }

        uiState.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun SelectedImageSection(
    imageUri: Uri?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        if (imageUri == null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "No image selected")
            }
        } else {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Selected Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun ResultCard(
    uiState: AssistantState
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "AI Assisted Result",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Detected Object: ${uiState.detectedObject ?: "-"}"
            )

            Text(
                text = "Image Confidence: ${
                    uiState.imageConfidence?.let {
                        String.format("%.2f", it * 100)
                    } ?: "-"
                }%"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Generated Text:")

            Text(
                text = uiState.generatedText ?: "-"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Text Model Result: ${uiState.textCategory ?: "-"}"
            )

            Text(
                text = "Text Confidence: ${
                    uiState.textConfidence?.let {
                        String.format("%.2f", it * 100)
                    } ?: "-"
                }%"
            )
        }
    }
}