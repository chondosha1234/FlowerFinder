package com.chondosha.flowerfinder.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.FileProvider
import com.chondosha.flowerfinder.R
import com.chondosha.flowerfinder.model.FlowerEntry
import java.io.File
import java.util.*

@Composable
fun PhotoListScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetail: () -> Unit
) {
    Column {
        PhotoList(
            onNavigateToDetail = onNavigateToDetail
        )

        takePictureButton(
            modifier = modifier
        )
    }

}

@Composable
fun PhotoList(
    modifier: Modifier = Modifier,
    onNavigateToDetail: () -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        item {
            PhotoEntryCell(
                flowerEntry = FlowerEntry(
                    id = UUID.randomUUID(),
                    label = "A flower",
                    date = Date()
                )
            ) {
                onNavigateToDetail()
            }
        }
    }
}

@Composable
fun takePictureButton(
    modifier: Modifier = Modifier
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { didTakePhoto ->
            if (didTakePhoto) {
                // use view model to create photo entry
            }
        }
    )
    val context = LocalContext.current
    Button(
        onClick = {
            val photoName = "IMG_${Date()}.JPG"
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

