package dev.etino.fcshared.attendance.client

import dev.etino.fcshared.networking.Endpoints
import dev.etino.fcshared.networking.LoginInterceptorPlugin
import dev.etino.fcshared.networking.PortalCookieStorage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class AttendanceClientImpl(
    private val loginInterceptorPlugin: LoginInterceptorPlugin
): AttendanceClient {

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

        loginInterceptorPlugin.setup(this)
    }

    override suspend fun getAttendanceItems(): String {
        val response = client.get(Endpoints.tableOverviewUrl.buildString()).bodyAsText()

        return response
    }

    override suspend fun getAttendanceItem(resourcePath: String): String {
        val url = Endpoints.attendanceUrl.buildString() + resourcePath
        val response = client.get(url).bodyAsText()

        return response
    }

}