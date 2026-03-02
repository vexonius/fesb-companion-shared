package dev.etino.fcshared.studomat.services

import com.fleeksoft.ksoup.Ksoup
import dev.etino.fcshared.networking.CustomCookieStorage
import dev.etino.fcshared.networking.NetworkServiceResult
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol

class StudomatService(private val client: HttpClient, private val cookieStorage: CustomCookieStorage) {

    suspend fun getStudomatData(): String {
        val response: HttpResponse = client.get("https://www.isvu.hr/studomat/hr/index")
        val success = response.status.value in 200..299
        val body = response.bodyAsText()

        checkIfLoggedIn(body)

        return if (success && body.isNotEmpty()) {
            //Log.d("StudomatService", "getStudomatData: ${body.substring(0, 100)}")
            body
        } else {
            //Log.d("StudomatService", "getStudomatData: Couldn't get Studomat data!")
            throw Throwable("Couldn't get Studomat data!")
        }
    }

    suspend fun getYearNames(): NetworkServiceResult.StudomatResult {
        val response: HttpResponse = client.get("https://www.isvu.hr/studomat/hr/studiranje/upisanegodine")
        val success = response.status.value in 200..299
        val body = response.bodyAsText()

        checkIfLoggedIn(body)

        return if (success && body.isNotEmpty()) {
            //Log.d("StudomatService", "getUpisaneGodine: ${body.substring(0, 100)}")
            NetworkServiceResult.StudomatResult.Success(body)
        } else {
            //Log.d("StudomatService", "getUpisaneGodine: Couldn't get upisane godine data!")
            NetworkServiceResult.StudomatResult.Failure(Throwable("Couldn't get upisane godine data!"))
        }
    }

    suspend fun getYearSubjects(href: String): NetworkServiceResult.StudomatResult {
        val response: HttpResponse = client.get("https://www.isvu.hr$href")
        val success = response.status.value in 200..299
        val body = response.bodyAsText()

        checkIfLoggedIn(body)

        return if (success && body.isNotEmpty()) {
            //Log.d("StudomatService", "getTrenutnuGodinuData: ${body.substring(0, 100)}")
            NetworkServiceResult.StudomatResult.Success(body)
        } else {
            //Log.d("StudomatService", "getTrenutnuGodinuData: Couldn't get current year data!")
            NetworkServiceResult.StudomatResult.Failure(Throwable("Couldn't get current year data!"))
        }
    }

    private suspend fun checkIfLoggedIn(body: String) {
        if (Ksoup.parse(body).title() == "Studomat - Prijava") {
            //Log.d("StudomatService", "getStudomatData: Couldn't get Studomat data!")
            clearSession()
            throw Throwable("Not logged in!")
        }
    }

    private suspend fun clearSession() = cookieStorage.clearISVUCookie()


    companion object {
        private val SCHEME = URLProtocol.HTTPS

        val targetUrl = URLBuilder().apply {
            protocol = SCHEME
            host = "www.isvu.hr"
            pathSegments = listOf("studomat")
        }.build()

    }
}
