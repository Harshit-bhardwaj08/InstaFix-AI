package project.hackmol.hackmolinstafix.tflite

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class ImageClassifier(private val context: Context) {

    // Load labels from assets folder (should be placed at app/src/main/assets/labels.txt)
    private val labels: List<String> = FileUtil.loadLabels(context, "labels.txt")
    private val interpreter: Interpreter

    init {
        // Load the model file from assets (should be placed at app/src/main/assets/model.tflite)
        val model = FileUtil.loadMappedFile(context, "model.tflite")
        interpreter = Interpreter(model)
    }

    fun classify(bitmap: Bitmap): String {
        // Step 1: Create a TensorImage from the raw bitmap
        val inputImage = TensorImage.fromBitmap(bitmap)

        // Step 2: Build an ImageProcessor to resize and normalize the image.
        // Adjust 224 and 224 if your model expects a different size.
        // The NormalizeOp below divides pixel values by 255.
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f))
            .build()

        // Process the image, which resizes and normalizes the bitmap.
        val processedImage = imageProcessor.process(inputImage)
        val inputBuffer = processedImage.buffer

        // Prepare output buffer.
        // Here we assume the output tensor has shape [1, labels.size] and type FLOAT32.
        // Adjust if your model outputs different dimensions.
        val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, labels.size), org.tensorflow.lite.DataType.FLOAT32)

        // Run inference. Use rewind() in case the buffer's position moved during processing.
        interpreter.run(inputBuffer, outputBuffer.buffer.rewind())

        // Find the index with the maximum probability.
        val outputArray = outputBuffer.floatArray
        val maxIndex = outputArray.indices.maxByOrNull { outputArray[it] } ?: -1

        return if (maxIndex != -1) {
            "${labels[maxIndex]} (Confidence: ${(outputArray[maxIndex] * 100).toInt()}%)"
        } else {
            "Unknown"
        }
    }

    fun close() {
        interpreter.close()
    }
}
