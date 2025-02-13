package dev.etino.fcshared.timetable.client

import dev.etino.fcshared.timetable.models.CalendarMetadataResponse

interface TimetableClient {

    suspend fun getTimetableEvents(params: Map<String, String>): String

    suspend fun getCalendarMetadata(params: Map<String, String>): List<CalendarMetadataResponse>

}