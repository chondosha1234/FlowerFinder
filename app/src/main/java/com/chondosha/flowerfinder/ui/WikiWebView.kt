package com.chondosha.flowerfinder.ui

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView


@Composable
fun FlowerWiki(
    modifier: Modifier = Modifier,
    url: String?
) {
    val fullUrl = "https://en.wikipedia.org/wiki/$url"
    WikiWebView(
        url = fullUrl
    )
}

@Composable
fun WikiWebView(
    modifier: Modifier = Modifier,
    url: String?
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                url?.let { loadUrl(it) }
            }
        },
        modifier = modifier.fillMaxSize()
    )
}