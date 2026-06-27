package com.example.tanuhaiassessment.core.network.api

import com.example.tanuhaiassessment.core.network.sto.ManifestResponse
import retrofit2.http.GET

interface ManifestApi {

    @GET("manifest.json")
    suspend fun getManifest(): ManifestResponse

}