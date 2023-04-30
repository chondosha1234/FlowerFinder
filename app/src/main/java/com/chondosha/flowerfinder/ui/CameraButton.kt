package com.chondosha.flowerfinder.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

private const val inputSize = 180
private const val NUM_CLASSES = 5
private const val MODEL_FILE_NAME = "model.tflite"
private val LABELS = listOf("Daisy", "Dandelion", "Rose", "Sunflower", "Tulip")

@Composable
fun CameraButton(
    modifier: Modifier = Modifier,
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

                val flowerEntry = FlowerEntry(
                    id = UUID.randomUUID(),
                    label = predictedLabel,
                    percentage = percentage,
                    date = Date(),
                    photoFileName = photoName
                )
                coroutineScope.launch {
                    flowerListViewModel.addFlowerEntry(flowerEntry)
                }
            }
        }
    )

    Button(
        onClick = {
            photoName = "IMG_${Date()}.JPG"
            val photoFile = File(context.applicationContext.filesDir, photoName)
            val photoUri = FileProvider.getUriForFile(
                context,
                "com.chondosha.flowerfinder.fileprovider",
                photoFile
            )
            launcher.launch(photoUri)
        }
    ) {
        Image(
            painter = painterResource(R.drawable.camera_button),
            contentDescription = null)
    }
}


private fun processPhoto(
    context: Context,
    photoName: String?
): Pair<String, Float> {

    val photoFile = File(context.applicationContext.filesDir, photoName)
    val photoBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)

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

    val tflite = Interpreter(loadModelFile(context, MODEL_FILE_NAME))
    val outputBuffer = Array(1) { FloatArray(NUM_CLASSES) }
    tflite.run(inputBuffer, outputBuffer)

    //val predictedLabel = LABELS[outputBuffer[0].indexOf(outputBuffer[0].maxOrNull()!!)]
    var maxIndex = 0
    for (i in 1 until outputBuffer[0].size) {
        if (outputBuffer[0][i] > outputBuffer[0][maxIndex]) {
            maxIndex = i
        }
    }
    val predictedLabel = LABELS[maxIndex]
    val percentage = outputBuffer[0][maxIndex]
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