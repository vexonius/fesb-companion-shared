package com.tstudioz.fax.fme.feature.timetable.view.schedule

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format

private val HourFormatter = LocalTime.Format { hour() }

@Composable
fun BasicSidebarLabel(
    time: LocalTime,
    modifier: Modifier = Modifier,
) {
    Text(
        text = time.format(HourFormatter),
        textAlign = TextAlign.End,
        modifier = modifier
            .fillMaxHeight()
            .padding(vertical = 0.dp, horizontal = 8.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}