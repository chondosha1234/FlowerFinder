package com.chondosha.flowerfinder.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.chondosha.flowerfinder.R

@Composable
fun NoMatchScreen(
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
            Column(
                modifier = modifier.padding(padding)
            ) {
                Image(
                    painter = painterResource(R.drawable.no_match_error),
                    contentDescription = null)
                Text(
                    text = stringResource(R.string.no_match)
                )
            }
        }
    )
}