package com.example.tanuhaiassessment.core.text

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.tanuhaiassessment.core.inference.text.TextClassificationResult
import com.example.tanuhaiassessment.core.inference.text.TextClassifier
import com.example.tanuhaiassessment.core.storage.model.ModelStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.LongBuffer
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.exp

@Singleton
class OnnxTextClassifier @Inject constructor(
    private val modelStorage: ModelStorage,
    private val tokenizer: BertTokenizer
) : TextClassifier {

    private val environment: OrtEnvironment by lazy {
        OrtEnvironment.getEnvironment()
    }

    private var session: OrtSession? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun classify(
        text: String
    ): TextClassificationResult = withContext(Dispatchers.Default) {

        val tokenizedInput = tokenizer.encode(text)

        val session = getSession()

        val inputShape = longArrayOf(
            1L,
            tokenizedInput.inputIds.size.toLong()
        )

        val inputIdsTensor = OnnxTensor.createTensor(
            environment,
            LongBuffer.wrap(tokenizedInput.inputIds),
            inputShape
        )

        val attentionMaskTensor = OnnxTensor.createTensor(
            environment,
            LongBuffer.wrap(tokenizedInput.attentionMask),
            inputShape
        )

        val tokenTypeIdsTensor = OnnxTensor.createTensor(
            environment,
            LongBuffer.wrap(tokenizedInput.tokenTypeIds),
            inputShape
        )

        val inputs = mapOf(
            "input_ids" to inputIdsTensor,
            "attention_mask" to attentionMaskTensor,
            "token_type_ids" to tokenTypeIdsTensor
        )

        try {
            session.run(inputs).use { result ->

                val logits = extractLogits(
                    result.get("logits").orElse(result[0]).value
                )

                val probabilities = softmax(logits)

                val bestIndex = probabilities.indices.maxBy {
                    probabilities[it]
                }

                TextClassificationResult(
                    category = mapLabel(bestIndex),
                    confidence = probabilities[bestIndex]
                )
            }
        } finally {
            inputIdsTensor.close()
            attentionMaskTensor.close()
            tokenTypeIdsTensor.close()
        }
    }

    private fun getSession(): OrtSession {

        session?.let {
            return it
        }

        val modelFile = modelStorage.getModelFile(
            "text_classifier"
        )

        val createdSession = environment.createSession(
            modelFile.absolutePath,
            OrtSession.SessionOptions()
        )

        session = createdSession

        return createdSession
    }

    private fun extractLogits(
        value: Any
    ): FloatArray {

        return when (value) {

            is Array<*> -> {
                val first = value.firstOrNull()

                when (first) {
                    is FloatArray -> first
                    is Array<*> -> {
                        first.mapNotNull {
                            it as? Float
                        }.toFloatArray()
                    }
                    else -> floatArrayOf(0f, 0f)
                }
            }

            is FloatArray -> value

            else -> floatArrayOf(0f, 0f)
        }
    }

    private fun softmax(
        logits: FloatArray
    ): FloatArray {

        val max = logits.maxOrNull() ?: 0f

        val expValues = logits.map {
            exp((it - max).toDouble())
        }

        val sum = expValues.sum()

        return expValues.map {
            (it / sum).toFloat()
        }.toFloatArray()
    }

    private fun mapLabel(
        index: Int
    ): String {

        return when (index) {
            0 -> "Normal generated note"
            1 -> "Spam-like generated note"
            else -> "Unknown"
        }
    }
}