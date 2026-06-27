package com.example.tanuhaiassessment.core.inference.image

import android.content.Context
import android.graphics.Bitmap
import com.example.tanuhaiassessment.core.storage.model.ModelStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import org.tensorflow.lite.Interpreter
import javax.inject.Inject
import androidx.core.graphics.scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder

class TFLiteImageClassifier @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val modelStorage: ModelStorage
): ImageClassifier{

    private var interpreter: Interpreter? = null

    private val labels: List<String> by lazy {
        context.assets.open("labels.txt")
            .bufferedReader()
            .readLines()
    }

    override suspend fun classify(image: Bitmap)
    : ImageClassificationResult =
        withContext(Dispatchers.Default) {

            val resizedBitmap =
                image.scale(224, 224)

            val inputBuffer =
                preprocess(resizedBitmap)

            val output =
                Array(1) {
                    FloatArray(1001)
                }

            getInterpreter().run(
                inputBuffer,
                output
            )

            val probabilities = output[0]

            val maxIndex =
                probabilities.indices.maxBy {
                    probabilities[it]
                }

            ImageClassificationResult(
                label = labels[maxIndex],
                confidence = probabilities[maxIndex]
            )
        }

    private fun getInterpreter(): Interpreter {

        if (interpreter != null) {
            return interpreter!!
        }

        val modelFile: File =
            modelStorage.getModelFile(
                "image_classifier"
            )

        interpreter =
            Interpreter(modelFile)

        return interpreter!!
    }

    private fun preprocess(
        bitmap: Bitmap
    ): ByteBuffer {

        val inputBuffer =
            ByteBuffer.allocateDirect(
                1 * 224 * 224 * 3 * 4
            )

        inputBuffer.order(
            ByteOrder.nativeOrder()
        )

        val pixels =
            IntArray(224 * 224)

        bitmap.getPixels(
            pixels,
            0,
            bitmap.width,
            0,
            0,
            bitmap.width,
            bitmap.height
        )

        pixels.forEach { pixel ->

            inputBuffer.putFloat(
                ((pixel shr 16 and 0xFF) / 255f)
            )

            inputBuffer.putFloat(
                ((pixel shr 8 and 0xFF) / 255f)
            )

            inputBuffer.putFloat(
                ((pixel and 0xFF) / 255f)
            )
        }

        return inputBuffer
    }
}