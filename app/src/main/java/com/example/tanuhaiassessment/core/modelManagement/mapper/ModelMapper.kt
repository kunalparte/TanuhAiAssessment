package com.example.tanuhaiassessment.core.modelManagement.mapper

import com.example.tanuhaiassessment.core.modelManagement.model.ModelMetadata
import com.example.tanuhaiassessment.core.network.sto.ModelMetadataDto
fun ModelMetadataDto.toDomain() =
    ModelMetadata(
        id,
        version,
        url,
        checksum
    )