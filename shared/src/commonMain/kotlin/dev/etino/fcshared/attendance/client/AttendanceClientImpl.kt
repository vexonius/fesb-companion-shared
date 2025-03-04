package dev.etino.fcshared.attendance.client

import dev.etino.fcshared.timetable.client.TimetableCookieStorage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol.Companion.HTTPS
import io.ktor.http.appendPathSegments

class AttendanceClientImpl: AttendanceClient {

    private val client = HttpClient {
        expectSuccess = true

        install(Logging) {
            level = LogLevel.ALL
        }

        install(HttpCookies) {
            storage = TimetableCookieStorage
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 15_000
        }
    }

    override suspend fun getAttendanceItems(): String {
        val response = client.get(tableOverviewUrl.buildString()).bodyAsText()

        return response
    }

    override suspend fun getAttendanceItem(resourcePath: String): String {
        val url = itemPartialUrl.buildString() + resourcePath
        val response = client.get(url).bodyAsText()

        return response
    }

    companion object {
        private const val baseUrl = "raspored.fesb.unist.hr"

        private  val tableOverviewUrl = URLBuilder(
            protocol = HTTPS,
            host = baseUrl,
            pathSegments = listOf(
                "part",
                "prisutnost",
                "opcenito",
                "tablica"
            )
        )

        private  val itemPartialUrl = URLBuilder(
            protocol = HTTPS,
            host = baseUrl
        )
    }

}