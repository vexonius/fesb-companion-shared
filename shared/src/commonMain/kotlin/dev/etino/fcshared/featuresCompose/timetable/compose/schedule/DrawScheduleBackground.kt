package dev.etino.fcshared.featuresCompose.timetable.compose.schedule

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalTime

fun DrawScope.drawScheduleBackground(
    minDayTime: LocalTime,
    numberOfDaysToShow: Int,
    numberOfHours: Int,
    hourHeight: Dp,
    dayWidth: Dp,
    dividerColor: Color
) {
    val dayWidthPx = dayWidth.toPx()
    val hourHeightPx = hourHeight.toPx()
    val firstHour = LocalTime(minDayTime.hour, 0, 0)
    val firstHourOffsetMinutes = (firstHour.toSecondOfDay() - minDayTime.toSecondOfDay()) / 60
    val firstHourOffset = (firstHourOffsetMinutes / 60f) * hourHeightPx

    val strokeWidthFullLine = 1.dp.toPx()
    val dashWidth = 2.dp.toPx()
    val dashHeight = 2.dp.toPx()
    val gapWidth = 2.dp.toPx()
    val pathEffect = PathEffect.dashPathEffect(intervals = floatArrayOf(dashWidth, gapWidth), phase = 0f)

    repeat(numberOfHours) {
        drawLine(
            dividerColor,
            start = Offset(0f, (it + 0.5f) * hourHeightPx + firstHourOffset),
            end = Offset(size.width, (it + 0.5f) * hourHeightPx + firstHourOffset),
            strokeWidth = dashHeight,
            pathEffect = pathEffect,
            alpha = 0.5f,
        )
    }

    repeat(numberOfHours + 1) {
        drawLine(
            dividerColor,
            start = Offset(0f, it * hourHeightPx + firstHourOffset),
            end = Offset(size.width, it * hourHeightPx + firstHourOffset),
            strokeWidth = strokeWidthFullLine
        )
    }

    repeat(numberOfDaysToShow + 1) {
        drawLine(
            dividerColor,
            start = Offset(it * dayWidthPx, 0f),
            end = Offset(it * dayWidthPx, size.height),
            strokeWidth = strokeWidthFullLine
        )
    }
}
