package com.chondosha.flowerfinder.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chondosha.flowerfinder.model.FlowerEntry
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

        takePictureButton()
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
    Button(
        onClick = { /*TODO*/ }
    ) {
        Text(text = "Take Picture")
    }
}