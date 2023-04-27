package com.chondosha.flowerfinder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

val LocalRepository = staticCompositionLocalOf<FlowerRepository> {
    error("No repository found!")
}

@Composable
fun ProvideRepository(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val repository = remember { FlowerRepository(context) }
    CompositionLocalProvider(LocalRepository provides repository) {
        content()
    }
}