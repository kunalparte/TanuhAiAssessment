package com.example.tanuhaiassessment.core.modelManagement.repository

import com.example.tanuhaiassessment.core.modelManagement.model.ModelMetadata
import com.example.tanuhaiassessment.core.modelManagement.mapper.toDomain
import com.example.tanuhaiassessment.core.network.api.ManifestApi
import javax.inject.Inject

class ModelRepositoryImpl @Inject constructor (
    private val api: ManifestApi
) : ModelRepository {

    override suspend fun fetchManifest(): List<ModelMetadata> {

        return api
            .getManifest()
            .models
            .map {
                it.toDomain()
            }
    }
}