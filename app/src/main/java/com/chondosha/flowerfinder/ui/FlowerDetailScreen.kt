package com.chondosha.flowerfinder.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.chondosha.flowerfinder.*
import java.io.File


@Composable
fun FlowerDetailScreen(
    modifier: Modifier = Modifier,
    flowerId: String?
) {

    val flowerDetailViewModel: FlowerDetailViewModel = viewModel(
    factory = FlowerDetailViewModelFactory(LocalRepository.current, flowerId)
    )

    val flower by flowerDetailViewModel.flower.collectAsState()

    val imagePainter = rememberAsyncImagePainter(
        model = flower?.photoFileName?.let {
            File(LocalContext.current.filesDir, it).toUri()
        },
        placeholder = painterResource(R.drawable.ic_launcher_background)
    )

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = { Text("FlowerFinder") }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier.padding(padding)
            ) {
                Image(
                    painter = imagePainter,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )

                Text(
                    text = "There is a 100% chance that this picture shows a flower"
                )

                Text(
                    text = "This will be a wiki link to that flower"
                )

                Text(
                    text = "The flowerEntry is ${flower?.label}"
                )
            }
        }
    )
}