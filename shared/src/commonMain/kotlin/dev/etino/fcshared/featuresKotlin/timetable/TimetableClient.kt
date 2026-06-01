package dev.etino.fcshared.featuresKotlin.timetable

import dev.etino.fcshared.featuresKotlin.networking.NetworkServiceResult

interface TimetableClient {

    suspend fun getTimetableEvents(params: Map<String, String>): NetworkServiceResult.TimeTableResult

    suspend fun fetchTimetableCalendar(params: HashMap<String, String>): NetworkServiceResult.TimeTableResult

}