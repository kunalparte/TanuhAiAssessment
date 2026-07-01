Tanuh AI Assessment

1. Project Overview
Objective

Tanuh AI Assessment demonstrates a reusable Android-first Edge AI platform capable of downloading, managing and executing AI models completely on-device without bundling them inside the application.

The application showcases an OTA (Over-The-Air) model management framework capable of:

Downloading AI models on first launch
Versioning AI models independently from application releases
Verifying model integrity before usage
Persisting models locally
Running inference completely offline
Providing a reusable SDK-like architecture for future AI models


2. Application Workflow -
User launches App
        │
        ▼
Manifest API
        │
        ▼
Available Models
        │
        ▼
Model Manager
        │
        ▼
Download Scheduler
        │
        ▼
WorkManager
        │
        ▼
Model Downloader Worker
        │
        ▼
Checksum Validation
        │
        ▼
Model Storage
        │
        ▼
Ready for Inference
        │
        ▼
────────────────────────────────────────
User selects Image
        │
        ▼
Bitmap Conversion
        │
        ▼
Image Classification
        │
        ▼
Generated Description
        │
        ▼
Text Classification
        │
        ▼
Combined AI Result



3. Features - 

OTA Model Download
Models are not bundled inside the APK. The application downloads them from GitHub on first launch.

Version Management
Each model maintains an independent version. New versions can be rolled out without updating the application.

Version Management
Each model maintains an independent version. New versions can be rolled out without updating the application.

Background Downloads
WorkManager guarantees reliable downloads even if the application is killed.

Local Model Cache
Downloaded models are persisted inside internal storage and reused across launches.

Edge AI Inference
All inference executes locally without requiring any backend service.


Extensible SDK Architecture
The complete model lifecycle has been separated into reusable components making it easy to plug additional AI models in future.



4. Dependencies Used
| Dependency          | Usage                          |
| ------------------- | ------------------------------ |
| Kotlin Coroutines   | Asynchronous operations        |
| StateFlow           | Reactive UI state management   |
| Jetpack Compose     | Declarative UI                 |
| Hilt                | Dependency Injection           |
| Retrofit            | Manifest API communication     |
| Gson Converter      | JSON serialization             |
| OkHttp              | OTA model downloading          |
| WorkManager         | Reliable background downloads  |
| DataStore           | Persisting model versions      |
| TensorFlow Lite     | Image classification inference |
| ONNX Runtime        | Text classification inference  |
| Coil                | Image loading                  |
| Lifecycle ViewModel | UI business logic              |
| Activity Compose    | Compose integration            |



5. Android Permissions

android.permission.INTERNET
Required for downloading AI models from remote storage.

android.permission.READ_MEDIA_IMAGES
Allows selecting images from device gallery (Android 13+).

android.permission.READ_EXTERNAL_STORAGE
Required for gallery access on Android versions below API 33.


6. AI Models -
   
Image Classification Model
| Property      | Value                                                        |
| ------------- | ------------------------------------------------------------ |
| Framework     | TensorFlow Lite                                              |
| Model         | MobileNet Image Classifier                                   |
| Download Type | OTA                                                          |
| Storage       | Internal Storage                                             |
| Purpose       | Detects the primary object present inside the selected image |

Why MobileNet?
Small model size
Optimized for Android
Fast inference
Excellent accuracy for edge devices


Text Classification Model
| Property      | Value                                                                   |
| ------------- | ----------------------------------------------------------------------- |
| Framework     | ONNX Runtime                                                            |
| Model         | Tiny BERT ONNX                                                          |
| Download Type | OTA                                                                     |
| Storage       | Internal Storage                                                        |
| Purpose       | Performs on-device text classification over generated image description |

Why ONNX?
Lightweight runtime
Excellent Android compatibility
Supports multiple model families
Easy future model migration



7. Architecture
                    UI Layer
────────────────────────────────────────

AssistantScreen

        │

AssistantViewModel

────────────────────────────────────────
             Domain Layer

ModelManager

ImageClassifier

TextClassifier

DownloadScheduler

────────────────────────────────────────
              Data Layer

ManifestApi

ModelRepository

VersionStorage

ModelStorage

ModelDownloader

ChecksumValidator

────────────────────────────────────────
           Background Layer

WorkManager

↓

ModelDownloaderWorker

────────────────────────────────────────
           Storage Layer

DataStore

Internal File Storage

────────────────────────────────────────
           AI Layer

TensorFlow Lite

↓

ONNX Runtime



8. OTA Model Download Flow
Application Start

        │

ModelManager

        │

Manifest API

        │

Model Repository

        │

Compare Versions

        │

Download Scheduler

        │

WorkManager

        │

Downloader Worker

        │

Download Model

        │

Validate SHA

        │

Save Model

        │

Save Version

        │

Ready for Inference


9. Image Inference Flow
Gallery

↓

Bitmap

↓

TensorFlow Lite

↓

Image Label

↓

Generate Caption

↓

ONNX Runtime

↓

Text Classification

↓

Compose UI



10. Project Structure
app
│
├── ui
│
├── featureAssist
│
├── core
│   │
│   ├── checksum
│   ├── inference
│   │     ├── image
│   │     └── text
│   ├── modelManagement
│   ├── network
│   ├── scheduler
│   ├── storage
│   ├── worker
│   └── common
│
├── di
│
└── assets


11. Class Responsibilities
MyApplication
Initializes Hilt and configures WorkManager with HiltWorkerFactory.

ManifestApi
Fetches remote model manifest describing available AI models.

ModelRepository
Responsible for retrieving model metadata from backend.

ModelManager
Coordinates model version checking, downloading and initialization.

DownloadScheduler
Schedules OTA model downloads through WorkManager.

ModelDownloaderWorker
Downloads AI models in the background while validating checksum and persisting version information.

ModelDownloader
Downloads binary model files using OkHttp.

ChecksumValidator
Validates downloaded models using SHA-256 checksum before trusting them.

ModelStorage
Provides read/write access to locally cached AI models.

VersionStorage
Stores model version information using DataStore.

ImageClassifier
Runs TensorFlow Lite image inference on selected images.

BertTokenizer
Converts raw text into BERT-compatible input tensors.

OnnxTextClassifier
Executes TinyBERT ONNX inference and predicts text category.

AssistantViewModel
Acts as the orchestration layer connecting UI, OTA model management and both AI inference pipelines.

AssistantScreen
Compose UI responsible for image selection, inference initiation and rendering combined AI results.



12. Design Decisions
OTA model delivery to minimize APK size.
Version-controlled AI models enabling independent upgrades.
Background downloads using WorkManager for reliability.
SHA-256 validation before trusting downloaded models.
Clean Architecture with dependency inversion.
Hilt for scalable dependency injection.
Separate inference layer enabling easy addition of future AI models.
SDK-ready modular design allowing reuse across applications.


