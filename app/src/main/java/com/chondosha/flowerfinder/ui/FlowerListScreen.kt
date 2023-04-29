package com.chondosha.flowerfinder.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import java.io.File
import java.util.*

@Composable
fun FlowerListScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetail: (UUID) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("FlowerFinder") }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FlowerList(
                    onNavigateToDetail = onNavigateToDetail,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                )

                TakePictureButton(
                    modifier = modifier
                )
            }
        }
    )
}

@Composable
fun FlowerList(
    modifier: Modifier = Modifier,
    flowerListViewModel: FlowerListViewModel = viewModel(
        factory = FlowerListViewModelFactory(LocalRepository.current)
    ),
    onNavigateToDetail: (UUID) -> Unit
) {
    val flowers by flowerListViewModel.flowers.collectAsState(emptyList())

    LazyColumn(
        modifier = modifier
    ) {
        items(flowers) { flower ->
            FlowerEntryCell(
                flowerEntry = flower
            ) {
                onNavigateToDetail(flower.id)
            }
        }
    }
}

@Composable
fun TakePictureButton(
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

