package dev.etino.fcshared.timetable.client

interface TimetableClient {

    suspend fun getTimetableEvents(params: Map<String, String>): String

    suspend fun getCalendarMetadata(params: Map<String, String>): String

}