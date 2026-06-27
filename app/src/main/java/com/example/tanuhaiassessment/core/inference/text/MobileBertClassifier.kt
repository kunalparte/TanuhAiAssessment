package com.example.tanuhaiassessment.core.inference.text

import com.example.tanuhaiassessment.core.storage.model.ModelStorage
import org.tensorflow.lite.Interpreter
import javax.inject.Inject

class MobileBertClassifier @Inject constructor(
    private val modelStorage: ModelStorage
) : TextClassifier {


    private var interpreter: Interpreter? = null

    private suspend fun getInterpreter(): Interpreter {

        if (interpreter == null) {

            val modelFile =
                modelStorage.getModelFile(
                    "text_classifier"
                )

            interpreter =
                Interpreter(modelFile)
        }
        return interpreter!!
    }


        override suspend fun classify(label: String): TextClassificationResult {
            val interpreter = getInterpreter()

            return TextClassificationResult(
                category = "Positive",
                confidence = 0.91f
            )
        }
    }