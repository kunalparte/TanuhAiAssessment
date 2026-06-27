package com.example.tanuhaiassessment.core.usecase

import com.example.tanuhaiassessment.core.common.Constants
import com.example.tanuhaiassessment.core.storage.model.ModelStorage
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class VerifyModelsReadyUseCase @Inject constructor(
    private val storage: ModelStorage

){

    operator fun invoke(): Boolean {

        return storage.modelExists(
            Constants.IMAGE_MODEL_ID
        ) &&
                storage.modelExists(
                    Constants.TEXT_MODEL_ID
                )
    }
}