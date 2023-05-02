package com.chondosha.flowerfinder.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chondosha.flowerfinder.R
import java.util.*

@Composable
fun NoMatchScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    val noMatchId = if (isSystemInDarkTheme()) R.drawable.no_match_error_dark else R.drawable.no_match_error_light

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
            )
        },
        content = { padding ->
            Column(
                modifier = modifier.padding(32.dp)
            ) {
                Image(
                    painter = painterResource(noMatchId),
                    contentDescription = null,
                    modifier = modifier
                        .align(Alignment.CenterHorizontally)
                        .size(128.dp)
                )
                Text(
                    text = stringResource(R.string.no_match),
                    modifier = modifier.padding(16.dp)
                )
                Row(
                    modifier = modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { onNavigateBack() },
                        modifier= modifier.padding(8.dp)
                    ) {
                        Text(text = "Go Back")
                    }
                    Button(
                        onClick = { /* todo */ },
                        modifier = modifier.padding(8.dp)
                    ) {
                        Text(text = "Report issue")
                    }
                }
            }
        }
    )
}
