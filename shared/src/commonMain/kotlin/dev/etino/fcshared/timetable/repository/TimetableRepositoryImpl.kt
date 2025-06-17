package dev.etino.fcshared.timetable.repository

import dev.etino.fcshared.timetable.client.TimetableClient
import dev.etino.fcshared.timetable.models.CalendarMetadataResponse
import dev.etino.fcshared.timetable.models.EventResponse
import dev.etino.fcshared.timetable.parser.TimetableParser

class TimetableRepositoryImpl(
    private val client: TimetableClient,
    private val parser: TimetableParser
): TimetableRepository {

    @Throws(Exception::class)
    override suspend fun getTimetableEvents(
        username: String,
        minDate: String,
        maxDate: String
    ): List<EventResponse> {
        val params: HashMap<String, String> = hashMapOf(
            "DataType" to "User",
            "DataId" to username,
            "MinDate" to minDate,
            "MaxDate" to maxDate
        )

        val data = client.getTimetableEvents(params)
        val response = parser.parse(data)

        return response
    }

    @Throws(Exception::class)
    override suspend fun getCalendarMetadata(
        dateFrom: String,
        dateTo: String
    ): List<CalendarMetadataResponse> {
        val params: HashMap<String, String> = hashMapOf(
            "FromDate" to dateFrom,
            "ToDate" to dateTo
        )

        return client.getCalendarMetadata(params)
    }

}