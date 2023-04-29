package com.chondosha.flowerfinder.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.chondosha.flowerfinder.*
import com.chondosha.flowerfinder.R
import kotlinx.coroutines.launch
import java.io.File


@Composable
fun FlowerDetailScreen(
    modifier: Modifier = Modifier,
    flowerId: String?,
    onNavigateToList: () -> Unit
) {

    val flowerDetailViewModel: FlowerDetailViewModel = viewModel(
    factory = FlowerDetailViewModelFactory(LocalRepository.current, flowerId)
    )

    val flower by flowerDetailViewModel.flower.collectAsState()

    val coroutineScope = rememberCoroutineScope()

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
                title = { Text("FlowerFinder") },
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            flower?.let { flowerDetailViewModel.deleteFlowerEntry(it) }
                            onNavigateToList()
                        }
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.delete_button),
                            contentDescription = "Delete"
                        )
                    }
                }
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
                    text = "There is a ${flower?.percentage}% chance that this picture shows a ${flower?.label}"
                )

                Text(
                    text = "For more information about this type of Flower check out the wikipedia page:"
                )

                Text(
                    text = "https://en.wikipedia.org/w/index.php?search=${flower?.label}"
                )
            }
        }
    )
}