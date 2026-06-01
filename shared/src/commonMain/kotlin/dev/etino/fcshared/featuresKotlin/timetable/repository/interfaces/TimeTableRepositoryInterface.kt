package dev.etino.fcshared.featuresKotlin.timetable.repository.interfaces

import dev.etino.fcshared.featuresKotlin.timetable.Event
import dev.etino.fcshared.featuresKotlin.timetable.TimeTableInfo
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.datetime.LocalDate

interface TimeTableRepositoryInterface {

    val events: SharedFlow<List<Event>>

    suspend fun fetchTimetable(user: String, startDate: String, endDate: String, shouldCache: Boolean): List<Event>

    suspend fun fetchTimeTableCalendar(startDate: String, endDate: String): Map<LocalDate, TimeTableInfo>

    suspend fun getCachedEvents(): List<Event>

}
