package com.chondosha.flowerfinder.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
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
    flowerListViewModel: FlowerListViewModel = viewModel(
        factory = FlowerListViewModelFactory(LocalRepository.current)
    ),
    onNavigateToDetail: () -> Unit
) {
    val flowers by flowerListViewModel.flowers.collectAsState(emptyList())

    LazyColumn(
        modifier = modifier
    ) {
        items(flowers) { flower ->
            FlowerEntryCell(
                flowerEntry = flower
            ) {
                onNavigateToDetail()
            }
        }
        item {
            FlowerEntryCell(
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
    modifier: Modifier = Modifier,
    flowerListViewModel: FlowerListViewModel = viewModel(
        factory = FlowerListViewModelFactory(LocalRepository.current)
    )
) {
    var photoName: String? = null

    val coroutineScope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { didTakePhoto ->
            if (didTakePhoto && photoName != null) {
                val flowerEntry = FlowerEntry(
                    id = UUID.randomUUID(),
                    label = "A flower",
                    date = Date(),
                    photoFileName = photoName
                )
                coroutineScope.launch {
                    flowerListViewModel.addFlowerEntry(flowerEntry)
                }
            }
        }
    )
    val context = LocalContext.current
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

