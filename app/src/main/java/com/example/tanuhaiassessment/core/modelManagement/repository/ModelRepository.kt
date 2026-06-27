package com.example.tanuhaiassessment.core.modelManagement.repository

import com.example.tanuhaiassessment.core.modelManagement.model.ModelMetadata

interface ModelRepository {
    suspend fun fetchManifest(): List<ModelMetadata>
}