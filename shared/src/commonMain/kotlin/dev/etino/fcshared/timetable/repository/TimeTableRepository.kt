package dev.etino.fcshared.timetable.repository

import dev.etino.fcshared.timetable.dao.TimeTableDao
import dev.etino.fcshared.timetable.parseTimetable
import dev.etino.fcshared.timetable.repository.interfaces.TimeTableRepositoryInterface
import dev.etino.fcshared.networking.NetworkServiceResult
import dev.etino.fcshared.timetable.Event
import dev.etino.fcshared.timetable.EventRoom
import dev.etino.fcshared.timetable.TimetableClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.time.Clock

class TimeTableRepository(
    private val timetableService: TimetableClient,
    private val timeTableDao: TimeTableDao
) : TimeTableRepositoryInterface {

    private val _events: MutableSharedFlow<List<Event>> = MutableSharedFlow(1)
    override val events: SharedFlow<List<Event>> = _events.asSharedFlow()


    init {
        observeEventsFromCache()
    }

    override suspend fun fetchTimetable(
        user: String,
        startDate: String,
        endDate: String,
        shouldCache: Boolean
    ): List<Event> {
        val params: HashMap<String, String> = hashMapOf(
            "DataType" to "User",
            "DataId" to user,
            "MinDate" to startDate,
            "MaxDate" to endDate
        )

        when (val result = timetableService.getTimetableEvents(params = params)) {
            is NetworkServiceResult.TimeTableResult.Success -> {
                val events = parseTimetable(result.data)

                if (shouldCache) {
                    insert(events)
                }

                return events
            }

            is NetworkServiceResult.TimeTableResult.Failure -> {
                throw Exception("Timetable fetching error")
            }
        }
    }

    /*override suspend fun fetchTimeTableCalendar(startDate: String, endDate: String): Map<LocalDate, TimeTableInfo> {
        val params: HashMap<String, String> = hashMapOf(
            "FromDate" to startDate,
            "ToDate" to endDate
        )

        *//*return when (val result = timetableService.fetchTimetableCalendar(params = params)) {
            is NetworkServiceResult.TimeTableResult.Success -> parseTimetableInfo(result.data)
            is NetworkServiceResult.TimeTableResult.Failure -> {
                throw Exception("TimetableInfo fetching error")
            }
        }*//*
        return emptyMap()
    }*/

    override suspend fun getCachedEvents(): List<Event> {
        return timeTableDao.getEvents().map { Event(it) }
    }

    private fun observeEventsFromCache() {
        CoroutineScope(Dispatchers.Default).launch {
            timeTableDao.getEventsAsync().collect { events ->
                _events.emit(events.map { Event(it) })
            }
        }
    }

    private suspend fun insert(classes: List<Event>) {
        timeTableDao.deleteAll()
        timeTableDao.insert(classes.map { EventRoom(it) })
    }

}

private fun Long.hasPassedMoreThan(seconds: Long): Boolean {
    return this + seconds * 1000 < Clock.System.now().toEpochMilliseconds()
}
