package dev.etino.fcshared.attendance.services

import dev.etino.fcshared.CustomCookieStorage
import dev.etino.fcshared.NetworkServiceResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.cookies.cookies
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Cookie

class AttendanceService(
) : AttendanceServiceInterface {

    private val baseURL = "https://raspored.fesb.unist.hr"
    private val client = HttpClient {
        expectSuccess = false

        install(HttpSend) {

        }
        install(HttpCookies) {
            storage = CustomCookieStorage()
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 10_000
        }
    }

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