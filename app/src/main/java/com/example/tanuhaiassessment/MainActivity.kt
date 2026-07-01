package com.example.tanuhaiassessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tanuhaiassessment.featureAssist.model.AssistantEvent
import com.example.tanuhaiassessment.featureAssist.ui.AssistantScreen
import com.example.tanuhaiassessment.featureAssist.vm.AssistantViewModel
import com.example.tanuhaiassessment.ui.theme.TanuhAiAssessmentTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: AssistantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            viewModel.handleIntent(AssistantEvent.DownloadModelsClicked)
            TanuhAiAssessmentTheme {

                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                val photoPickerLauncher =
                    rememberLauncherForActivityResult(
                        contract =
                            ActivityResultContracts.PickVisualMedia()
                    ) { uri ->

                        uri?.let {

                            viewModel.handleIntent(AssistantEvent.ImageSelected(it))
                        }
                    }

                AssistantScreen(
                    uiState = uiState,
                    onEvent = {
                        when(it){
                            is AssistantEvent.PickImageClicked -> {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            }
                            else -> {
                                viewModel.handleIntent(it)
                            }
                        }

                    }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TanuhAiAssessmentTheme {
        Greeting("Android")
    }
}