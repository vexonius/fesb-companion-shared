package dev.etino.fcshared.attendance.repository

import dev.etino.fcshared.attendance.client.AttendanceClient
import dev.etino.fcshared.attendance.models.AttendanceItemResponse
import dev.etino.fcshared.attendance.parser.AttendanceParser
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

class AttendanceRepositoryImpl(
    private val client: AttendanceClient,
    private val parser: AttendanceParser
) : AttendanceRepository {

    @Throws(Exception::class)
    override suspend fun getAttendance(): List<AttendanceItemResponse> {
        val response = client.getAttendanceItems()
        val tableItems = parser.parse(response)

        val items = runBlocking {
            tableItems
                .map { item ->
                    async {
                        try {
                            val detailedResponse = client.getAttendanceItem(item.partialUrl)
                            parser.parseItem(detailedResponse, item.`class`, item.semester)
                        } catch (e: Exception) {
                            println("Error fetching or parsing item: ${item.partialUrl}. Error: ${e.message}")
                            null
                        }
                    }
                }
                .awaitAll()
                .filterNotNull()
                .flatten()
        }

        return items
    }

}