package dev.etino.fcshared

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.http.Cookie
import io.ktor.http.Url

class TimetableClientImpl: TimetableClient {

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

    override suspend fun getTimetableEvents(params: Map<String, String>): String {
        val endpointUrl  = "$baseURL/part/raspored/kalendar"

        val result =  client.get(endpointUrl) {
            url {
                for ((key, value) in params) {
                    parameters.append(key, value)
                }
            }
        }.body<String>()

        println(result)

        return result
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

class CustomCookieStorage(
    private val defaultStorage: CookiesStorage = AcceptAllCookiesStorage()
): CookiesStorage {

    override suspend fun get(requestUrl: Url): List<Cookie> {
        val stored = defaultStorage.get(requestUrl)
        val added = stored.toMutableList().plus(Cookie(
            name = "Fesb.AuthCookie",
            domain = "fesb.unist.hr",
            value = "3050C066E5CAE5D15AE6565DB7FEAA61626E611D03E2F1D021E5F70B5037C3201ABA38CDCA66CC2139097248EE3612B88DEE4878205CFBD27F101F29A41110032E88B50C5037CF98E63C4913A4B9A90EB17F1E87E206AE91418E4022BA84297758335666747D30E51758C4E95C70D265C4533C3E6EB2F1C01A4555E10FA3AD93AA376E8DBE44852D7F0E2A2D0CD849E26BC36D2969E910AB09C43BEF71C3DB672B2CC0CCDD5551D3289E7C61B9DD7977B76567E9391F6039A43AB7C23344EAD0"
        ))
        return added
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        defaultStorage.addCookie(requestUrl, cookie)
    }

    override fun close() {
        defaultStorage.close()
    }

}

interface HttpClientInterceptor {
    fun intercept(context: HttpRequestBuilder): HttpRequestBuilder
}