package com.chondosha.flowerfinder.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chondosha.flowerfinder.R
import com.chondosha.flowerfinder.model.FlowerEntry

@Composable
fun PhotoEntryCell(
    flowerEntry: FlowerEntry,
    modifier: Modifier = Modifier,
    onClickEntry: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onClickEntry() }
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        //change this image
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.flower_label, flowerEntry.label)
        )
    }
}