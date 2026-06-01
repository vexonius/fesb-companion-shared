package dev.etino.fcshared.featuresCompose.timetable.compose.schedule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalTime

@Composable
fun ScheduleSidebar(
    hourHeight: Dp,
    modifier: Modifier = Modifier,
    minTime: LocalTime = LocalTime(0, 0, 0),
    maxTime: LocalTime = LocalTime(23, 59, 59),
    label: @Composable (time: LocalTime) -> Unit = { BasicSidebarLabel(time = it) },
) {
    val numMinutes = minTime.until(maxTime, DateTimeUnit.MINUTE) + 1
    val numHours = numMinutes / 60
    val firstHour = LocalTime(minTime.hour, 0, 0)
    val firstHourOffsetMinutes =
        if (firstHour == minTime) 0 else minTime.until(LocalTime(firstHour.hour + 1, 0, 0), DateTimeUnit.MINUTE) + 1
    val firstHourOffset = hourHeight * (firstHourOffsetMinutes / 60f)
    val startTime = if (firstHour == minTime) firstHour else LocalTime(firstHour.hour + 1, 0, 0)
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(firstHourOffset))
        repeat(numHours) { i ->
            Box(modifier = Modifier.height(hourHeight)) {
                label(LocalTime(startTime.hour + i, 0, 0))
            }
        }
    }
}