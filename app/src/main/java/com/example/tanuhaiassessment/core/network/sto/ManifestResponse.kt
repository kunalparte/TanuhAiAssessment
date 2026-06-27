package com.example.tanuhaiassessment.core.network.sto

data class ManifestResponse(
    val models: List<ModelMetadataDto>
)


data class ModelMetadataDto(
    val id: String,
    val version: String,
    val url: String,
    val checksum: String
)