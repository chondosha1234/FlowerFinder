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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
    onNavigateToList: () -> Unit,
    onNavigateToWiki: (String) -> Unit
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
                title = { Text(stringResource(R.string.app_name)) },
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
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(256.dp)
                        .padding(16.dp)
                )

                Text(
                    text = "There is a ${flower?.percentage}% chance that this picture shows a ${flower?.label}",
                    modifier = modifier.padding(16.dp)
                )

                Text(
                    text = "For more information about this type of Flower check out the wikipedia page:",
                    modifier = modifier.padding(16.dp)
                )

                Button(
                    onClick = {
                        onNavigateToWiki("${flower?.label}")
                    },
                    modifier = modifier.padding(16.dp)
                ) {
                    Text(text = "Go To Wiki")
                }
            }
        }
    )
}