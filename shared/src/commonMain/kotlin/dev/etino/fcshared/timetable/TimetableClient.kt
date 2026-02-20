package dev.etino.fcshared.timetable

import dev.etino.fcshared.networking.NetworkServiceResult
import io.ktor.client.HttpClient

interface TimetableClient {

    suspend fun getTimetableEvents(params: Map<String, String>): NetworkServiceResult.TimeTableResult

    suspend fun fetchTimetableCalendar(params: HashMap<String, String>): NetworkServiceResult.TimeTableResult

}