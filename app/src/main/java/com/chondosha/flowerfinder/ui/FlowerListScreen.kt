package com.chondosha.flowerfinder.ui


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.*
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
    onNavigateToSettings: () -> Unit
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
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                )

                CameraButton(
                    onNavigateToDetail = onNavigateToDetail,
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


