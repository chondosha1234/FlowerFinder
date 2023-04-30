package com.chondosha.flowerfinder.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chondosha.flowerfinder.R

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = modifier.padding(padding)
            ) {
                item {
                    Text(text = stringResource(R.string.change_themes))
                }
                item {
                    Text(text = stringResource(R.string.change_notifications))
                }
            }
        }
    )
}