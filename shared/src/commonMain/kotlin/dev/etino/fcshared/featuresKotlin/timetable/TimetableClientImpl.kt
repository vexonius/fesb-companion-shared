package dev.etino.fcshared.featuresKotlin.timetable

import dev.etino.fcshared.featuresKotlin.networking.NetworkServiceResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.http.isSuccess

class TimetableClientImpl(val client: HttpClient) : TimetableClient {

    private val baseURL = "https://raspored.fesb.unist.hr"

    override suspend fun getTimetableEvents(params: Map<String, String>): NetworkServiceResult.TimeTableResult {
        val endpointUrl = "$baseURL/part/raspored/kalendar"

        val result = client.get(endpointUrl) {
            url {
                for ((key, value) in params) {
                    parameters.append(key, value)
                }
            }
        }

        val value = result.body<String>()
        val success = result.status.isSuccess()


        if (!success || value.isEmpty()) {
            return NetworkServiceResult.TimeTableResult.Failure(Throwable("Failed to fetch schedule"))
        }

        return NetworkServiceResult.TimeTableResult.Success(value)
    }

    override suspend fun fetchTimetableCalendar(params: HashMap<String, String>): NetworkServiceResult.TimeTableResult {
        val endpointUrl = "$baseURL/raspored/periodi-u-mjesecu-json"

        val result = client.get(endpointUrl) {
            url {
                for ((key, value) in params) {
                    parameters.append(key, value)
                }
            }
        }
        val value = result.body<String>()
        val success = result.status.isSuccess()


        if (!success || value.isEmpty()) {
            return NetworkServiceResult.TimeTableResult.Failure(Throwable("Failed to fetch schedule"))
        }

        return NetworkServiceResult.TimeTableResult.Success(value)
    }

}

interface HttpClientInterceptor {
    fun intercept(context: HttpRequestBuilder): HttpRequestBuilder
}