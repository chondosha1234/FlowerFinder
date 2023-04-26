package com.chondosha.flowerfinder.ui

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PhotoListScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetail: () -> Unit
) {
    Button(
        onClick = { onNavigateToDetail() }
    ) {
        Text(text = "Go to other screen")
    }
}