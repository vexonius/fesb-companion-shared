package dev.etino.fcshared.timetable

import androidx.compose.ui.graphics.Color


fun TimetableType.color(): Color {
    val accentRed = Color(0xFFFF5252)
    val accentBlue = Color(0xFF29B6F6)
    val accentGreen = Color(0xFF72ffa5)
    val accentPurple = Color(0xFFBA68C8)
    val accentGrey = Color(0xFF757575)
    return when (this) {
        TimetableType.PREDAVANJE -> accentBlue
        TimetableType.AUDITORNA_VJEZBA -> accentGreen
        TimetableType.KOLOKVIJ -> accentPurple
        TimetableType.LABORATORIJSKA_VJEZBA -> accentRed
        TimetableType.KONSTRUKCIJSKA_VJEZBA -> accentGrey
        TimetableType.SEMINAR -> accentBlue
        TimetableType.ISPIT -> accentPurple
        TimetableType.OTHER -> accentBlue
    }
}
