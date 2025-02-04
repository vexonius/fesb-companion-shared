package dev.etino.fcshared.timetable.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Cookie
import io.ktor.http.Url

class TimetableClientImpl: TimetableClient {

    private val baseURL = "https://raspored.fesb.unist.hr"
    private val client = HttpClient {
        expectSuccess = false

        install(HttpSend) {

        }
        install(HttpCookies) {
            storage = TimetableCookieStorage()
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 10_000
        }
    }

    override suspend fun getTimetableEvents(params: Map<String, String>): String {
        val endpointUrl  = "$baseURL/part/raspored/kalendar"

        val result =  client.get(endpointUrl) {
            url {
                for ((key, value) in params) {
                    parameters.append(key, value)
                }
            }
        }

        return result.bodyAsText()
    }

    override suspend fun fetchTimetableCalendar(params: HashMap<String, String>): String {
        val endpointUrl = "$baseURL/raspored/periodi-u-mjesecu-json"

        return client.get(endpointUrl) {
            url {
                for ((key, value) in params) {
                    parameters.append(key, value)
                }
            }
        }.body<String>()
    }

}