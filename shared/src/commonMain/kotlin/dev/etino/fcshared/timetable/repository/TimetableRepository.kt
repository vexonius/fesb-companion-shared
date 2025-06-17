package dev.etino.fcshared.timetable.repository

import dev.etino.fcshared.timetable.models.CalendarMetadataResponse
import dev.etino.fcshared.timetable.models.EventResponse

interface TimetableRepository {

    @Throws(Exception::class)
    suspend fun getTimetableEvents(username: String, minDate: String, maxDate: String): List<EventResponse>

    @Throws(Exception::class)
    suspend fun getCalendarMetadata(dateFrom: String, dateTo: String): List<CalendarMetadataResponse>

}