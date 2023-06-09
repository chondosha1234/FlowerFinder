package com.chondosha.flowerfinder.ui


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chondosha.flowerfinder.FlowerListViewModel
import com.chondosha.flowerfinder.FlowerListViewModelFactory
import com.chondosha.flowerfinder.LocalRepository
import java.util.*
import com.chondosha.flowerfinder.R

@Composable
fun FlowerListScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetail: (UUID) -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToNoMatch: () -> Unit
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    OptionsMenu(
                        onNavigateToAbout = onNavigateToAbout,
                        onNavigateToSettings = onNavigateToSettings
                    )
                }
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
                        .padding(4.dp)
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                )

                CameraButton(
                    onNavigateToDetail = onNavigateToDetail,
                    onNavigateToNoMatch = onNavigateToNoMatch,
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
        if (flowers.isEmpty()) {
            item {
                Text(
                    text = "No entries to display",
                    textAlign = TextAlign.Center,
                    modifier = modifier.fillMaxSize().fillMaxHeight()
                )
            }
        } else {
            items(flowers) { flower ->
                FlowerEntryCell(
                    flowerEntry = flower,
                    onClickEntry = {
                        onNavigateToDetail(flower.id)
                    },
                    modifier = modifier
                        .padding(4.dp)
                )
            }
        }
    }
}


