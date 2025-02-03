package dev.etino.fcshared.timetable.models


data class Event(
    val id: String,
    val name: String,
    val shortName: String,
    val professor: String = "",
    val eventType: TimetableType = TimetableType.OTHER,
    val groups: String = "",
    val classroom: String = "",
    val start: String,
    val end: String,
    val description: String? = null,
    val recurring: Boolean = false,
    val recurringType: Recurring = Recurring.UNDEFINED,
    val recurringUntil: String = "",
    val studyCode: String = "",
)