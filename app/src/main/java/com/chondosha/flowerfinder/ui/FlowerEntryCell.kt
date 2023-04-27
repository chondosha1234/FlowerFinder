package com.chondosha.flowerfinder.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.chondosha.flowerfinder.R
import com.chondosha.flowerfinder.model.FlowerEntry
import java.io.File

@Composable
fun FlowerEntryCell(
    flowerEntry: FlowerEntry,
    modifier: Modifier = Modifier,
    onClickEntry: () -> Unit
) {
    val imagePainter = rememberAsyncImagePainter(
        model = flowerEntry.photoFileName?.let {
            File(LocalContext.current.filesDir, it).toUri()
        },
        placeholder = painterResource(R.drawable.ic_launcher_background)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onClickEntry() }
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        //change this image
        Image(
            painter = imagePainter,
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = stringResource(R.string.flower_label, flowerEntry.label)
        )
    }
}