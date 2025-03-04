package dev.etino.fcshared.attendance.client

import dev.etino.fcshared.networking.LoginInterceptorPlugin
import dev.etino.fcshared.networking.PortalCookieStorage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol.Companion.HTTPS

class AttendanceClientImpl: AttendanceClient {

    private val client = HttpClient {
        expectSuccess = true

        install(Logging) {
            level = LogLevel.ALL
        }

        install(HttpCookies) {
            storage = PortalCookieStorage
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 15_000
        }

        LoginInterceptorPlugin().setup(this)
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

        private  val itemPartialUrl = URLBuilder(
            protocol = HTTPS,
            host = baseUrl
        )

        val tableOverviewUrl = URLBuilder(
            protocol = HTTPS,
            host = baseUrl,
            pathSegments = listOf(
                "part",
                "prisutnost",
                "opcenito",
                "tablica"
            )
        )
    }

}