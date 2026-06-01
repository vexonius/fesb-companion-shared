package dev.etino.fcshared.featuresCompose.timetable.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.char

class TimetableDateFormatter {

    companion object {
        val hourFormatter = LocalTime.Format { hour() }

        val dayFormatter = LocalDate.Format {
            day()
            char('.')
            char(' ')
        }
    }

}