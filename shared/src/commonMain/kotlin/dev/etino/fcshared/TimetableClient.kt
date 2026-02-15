package dev.etino.fcshared

import io.ktor.client.HttpClient

interface TimetableClient {

    suspend fun getTimetableEvents(params: Map<String, String>): String

    suspend fun fetchTimetableCalendar(params: HashMap<String, String>): String

}