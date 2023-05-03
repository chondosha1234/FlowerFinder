package com.chondosha.flowerfinder.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chondosha.flowerfinder.FlowerListViewModel
import com.chondosha.flowerfinder.FlowerListViewModelFactory
import com.chondosha.flowerfinder.LocalRepository
import com.chondosha.flowerfinder.R
import com.chondosha.flowerfinder.model.FlowerEntry
import kotlinx.coroutines.launch
import org.tensorflow.lite.Interpreter
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*
import kotlin.math.exp

private const val inputSize = 180
private const val NUM_CLASSES = 5
private const val MODEL_FILE_NAME = "model.tflite"
private val LABELS = listOf("Daisy", "Dandelion", "Rose", "Sunflower", "Tulip")

private const val EXP_MAX_INPUT = 40f

@Composable
fun CameraButton(
    modifier: Modifier = Modifier,
    onNavigateToDetail: (UUID) -> Unit,
    onNavigateToNoMatch: () -> Unit,
    flowerListViewModel: FlowerListViewModel = viewModel(
        factory = FlowerListViewModelFactory(LocalRepository.current)
    )
) {

    var photoName: String? = null

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { didTakePhoto ->
            if (didTakePhoto && photoName != null) {

                val (predictedLabel, percentage) = processPhoto(context, photoName)

                if (percentage >= 0.60f) {
                    val flowerEntry = FlowerEntry(
                        id = UUID.randomUUID(),
                        label = predictedLabel,
                        percentage = percentage,
                        date = Date(),
                        photoFileName = photoName
                    )
                    coroutineScope.launch {
                        flowerListViewModel.addFlowerEntry(flowerEntry)
                        onNavigateToDetail(flowerEntry.id)
                    }
                } else {
                    onNavigateToNoMatch()
                }
            }
        }
    )

    Button(
        shape = CircleShape,
        onClick = {
            photoName = "IMG_${Date()}.JPG"
            val photoFile = File(context.applicationContext.filesDir, photoName)
            val photoUri = FileProvider.getUriForFile(
                context,
                "com.chondosha.flowerfinder.fileprovider",
                photoFile
            )
            launcher.launch(photoUri)
        },
        modifier = modifier.padding(32.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.camera_button),
            contentDescription = null,
            modifier = modifier
                .padding(16.dp)
                .size(32.dp)
        )
    }
}


private fun processPhoto(
    context: Context,
    photoName: String?
): Pair<String, Float> {

    // get file of photo that was just taken and change it to a bitmap
    val photoFile = File(context.applicationContext.filesDir, photoName)
    val photoBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)

    // resize the bitmap to match the ML model's image size and make input buffer
    val resizedBitmap = Bitmap.createScaledBitmap(photoBitmap, inputSize, inputSize, true)
    val inputBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3).apply {
        order(ByteOrder.nativeOrder())
    }
    val intValues = IntArray(inputSize * inputSize)
    resizedBitmap.getPixels(
        intValues, 0, inputSize, 0, 0, inputSize, inputSize
    )
    var pixel = 0
    for (i in 0 until inputSize){
        for (j in 0 until inputSize){
            val value = intValues[pixel++]
            inputBuffer.putFloat(((value shr 16) and 0xFF) / 255.0f)
            inputBuffer.putFloat(((value shr 8) and 0xFF) / 255.0f)
            inputBuffer.putFloat((value and 0xFF) / 255.0f)
        }
    }

    // load in the model and make an output buffer for results (2D array, 1 row that holds Float array of prediction %)
    val tflite = Interpreter(loadModelFile(context, MODEL_FILE_NAME))
    val outputBuffer = Array(1) { FloatArray(NUM_CLASSES) }
    tflite.run(inputBuffer, outputBuffer)

    val probabilities = softmax(outputBuffer[0])

    for (i in 0 until outputBuffer[0].size) {
        Log.d("Model", "Percent for category ${LABELS[i]} is ${probabilities[i]}")
    }

    //get the index of the max percentage, which will be the prediction
    var maxIndex = 0
    for (i in probabilities.indices) {
        if (probabilities[i] > probabilities[maxIndex]) {
            maxIndex = i
        }
    }
    val predictedLabel = LABELS[maxIndex]
    val percentage = probabilities[maxIndex]
    return Pair(predictedLabel, percentage)
}

private fun loadModelFile(
    context: Context,
    modelFileName: String
): MappedByteBuffer {
    val fileDescriptor = context.assets.openFd(modelFileName)
    val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
    val fileChannel = inputStream.channel
    val startOffset = fileDescriptor.startOffset
    val declaredLength = fileDescriptor.declaredLength
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
}

private fun softmax(logits: FloatArray): FloatArray {
    val maxLogit = logits.maxOrNull() ?: 0f
    val logitsExp = logits.map { exp(it - maxLogit) }
    val sumExp = logitsExp.sum()
    return logitsExp.map { it / sumExp }.toFloatArray()
}

// Exponential function with added stability to avoid overflow errors
private fun exp(x: Float): Float {
    val EXP_MAX_OUTPUT = exp(EXP_MAX_INPUT.toDouble()).toFloat()
    return if (x > EXP_MAX_INPUT) {
        EXP_MAX_OUTPUT
    } else {
        exp(x.toDouble()).toFloat()
    }
}
