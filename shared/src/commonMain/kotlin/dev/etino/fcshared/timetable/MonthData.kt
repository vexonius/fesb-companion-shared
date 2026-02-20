package dev.etino.fcshared.timetable

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.YearMonth

data class MonthData(
    val currentMonth: YearMonth,
    val startMonth: YearMonth,
    val endMonth: YearMonth,
    val firstDayOfWeek: DayOfWeek
)
