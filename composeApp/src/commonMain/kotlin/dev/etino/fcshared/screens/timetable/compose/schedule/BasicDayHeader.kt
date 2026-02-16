package dev.etino.fcshared.screens.timetable.compose.schedule

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format


private val DayFormatter = LocalDate.Format { day() }

@Composable
fun BasicDayHeader(day: LocalDate) {
    Text(
        text = dayOfWeekHr(day.dayOfWeek.ordinal) + " " + day.format(DayFormatter),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

fun dayOfWeekHr(day: Int): String =
    when (day) {
        0 -> "Ponedjeljak"
        1 -> "Utorak"
        2 -> "Srijeda"
        3 -> "Četvrtak"
        4 -> "Petak"
        5 -> "Subota"
        6 -> "Nedjelja"
        else -> ""
    }