package com.chondosha.flowerfinder.ui


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
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
        items(flowers) { flower ->
            FlowerEntryCell(
                flowerEntry = flower,
                onClickEntry = {
                    onNavigateToDetail(flower.id)
                },
                modifier = modifier
                    .padding(4.dp)
                    .drawBehind {
                        drawLine(
                            color = Color.Gray,
                            start = Offset(0f, size.height + 16),
                            end = Offset(size.width, size.height + 16),
                            strokeWidth = 1.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
            )
        }
    }
}


