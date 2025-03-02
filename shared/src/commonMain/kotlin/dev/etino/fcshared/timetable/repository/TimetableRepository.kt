package dev.etino.fcshared.timetable.repository

import dev.etino.fcshared.timetable.models.EventResponse

interface TimetableRepository {

    @Throws(Exception::class)
    suspend fun getTimetableEvents(username: String, minDate: String, maxDate: String): List<EventResponse>

}