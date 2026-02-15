package dev.etino.fcshared.attendance.services

import dev.etino.fcshared.networking.NetworkServiceResult
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

class AttendanceService(val client: HttpClient) : AttendanceServiceInterface {

    private val baseURL = "https://raspored.fesb.unist.hr"

    override suspend fun fetchAllAttendance(): NetworkServiceResult.AttendanceFetchResult {

        val response: HttpResponse = client.get("$baseURL/part/prisutnost/opcenito/tablica")
        val success = response.status.value in 200..299
        val data = response.bodyAsText()

        return if (success) {
            NetworkServiceResult.AttendanceFetchResult.Success(data)
        } else {
            NetworkServiceResult.AttendanceFetchResult.Failure(Throwable("Failed to fetch attendance"))
        }
    }

    override suspend fun fetchAttendance(classId: String): NetworkServiceResult.AttendanceFetchResult {

        val response = client.get("$baseURL${classId}")
        val success = response.status.value in 200..299
        val data = response.bodyAsText()

        if (!success) {
            return NetworkServiceResult.AttendanceFetchResult.Failure(Throwable("Failed to fetch attendance details"))
        }

        return NetworkServiceResult.AttendanceFetchResult.Success(data)
    }
}