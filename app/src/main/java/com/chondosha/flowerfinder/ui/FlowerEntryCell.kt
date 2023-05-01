package com.chondosha.flowerfinder.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.chondosha.flowerfinder.R
import com.chondosha.flowerfinder.model.FlowerEntry
import java.io.File
import java.text.DateFormat
import java.util.*

@Composable
fun FlowerEntryCell(
    flowerEntry: FlowerEntry,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.body2,
    onClickEntry: () -> Unit
) {
    val imagePainter = rememberAsyncImagePainter(
        model = flowerEntry.photoFileName?.let {
            File(LocalContext.current.filesDir, it).toUri()
        },
        placeholder = painterResource(R.drawable.no_match_error_light)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onClickEntry() }
            .padding(vertical = 4.dp, horizontal = 16.dp)
            .drawBehind {
                drawLine(
                    color = Color.Gray,
                    start = Offset(0f, size.height + 16),
                    end = Offset(size.width, size.height + 16),
                    strokeWidth = 1.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
    ) {
        Image(
            painter = imagePainter,
            contentDescription = null,
            modifier = Modifier.size(72.dp)
        )
        Column(
            modifier = modifier.padding(8.dp)
        ) {
            Text(
                text = stringResource(R.string.flower_label, flowerEntry.label),
                style = textStyle
            )
            Text(
                text = DateFormat.getDateTimeInstance(
                    DateFormat.FULL,
                    DateFormat.SHORT,
                    Locale.getDefault()
                ).format(flowerEntry.date),
                style = textStyle
            )
        }

    }
}