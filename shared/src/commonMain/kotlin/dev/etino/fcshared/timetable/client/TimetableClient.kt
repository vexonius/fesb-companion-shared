package dev.etino.fcshared.timetable.client

interface TimetableClient {

    suspend fun getTimetableEvents(params: Map<String, String>): String

    suspend fun fetchCalendarMetadata(params: HashMap<String, String>): String

}