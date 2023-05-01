package com.chondosha.flowerfinder.ui

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.chondosha.flowerfinder.R


@Composable
fun OptionsMenu(
    modifier: Modifier = Modifier,
    onNavigateToAbout: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(
        onClick = { expanded = true }
    ) {
        Icon(
            painter = painterResource(R.drawable.overflow_menu),
            contentDescription = "Menu"
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(onClick = {
            onNavigateToAbout()
        }) {
            Text(stringResource(R.string.about))
        }
        DropdownMenuItem(onClick = {
            onNavigateToSettings()
        }) {
            Text(stringResource(R.string.settings))
        }
    }
}