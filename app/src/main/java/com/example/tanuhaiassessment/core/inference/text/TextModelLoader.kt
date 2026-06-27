package com.example.tanuhaiassessment.core.inference.text

import com.example.tanuhaiassessment.core.storage.model.ModelStorage
import org.tensorflow.lite.Interpreter
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TextModelLoader @Inject constructor(
    private val modelStorage: ModelStorage
) {

    private var interpreter: Interpreter? = null

    fun getInterpreter(): Interpreter {

        if (interpreter == null) {

            val file =
                modelStorage.getModelFile(
                    "text_classifier"
                )

            interpreter =
                Interpreter(file)
        }

        return interpreter!!
    }
}