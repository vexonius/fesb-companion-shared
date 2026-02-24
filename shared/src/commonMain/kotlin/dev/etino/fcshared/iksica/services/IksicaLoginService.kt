package dev.etino.fcshared.iksica.services

import com.fleeksoft.ksoup.Ksoup
import dev.etino.fcshared.networking.NetworkServiceResult
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.Parameters
import io.ktor.http.Url
import io.ktor.http.isSuccess

class IksicaLoginService(
    private val client: HttpClient,
    private var currentUrl: Url?,
    private var authState: String,
    private var sAMLResponse: String
) : IksicaLoginServiceInterface {

    private var successfulIsspLoginAlready = false
    private var successfulAaieduLoginAlready = false

    override suspend fun getAuthState(): NetworkServiceResult.IksicaResult {
        val response: HttpResponse = client.get("https://issp.srce.hr/auth/loginaai")
        val success = response.status.value in 200..299
        val body = response.bodyAsText()
        val doc = Ksoup.parse(body)

        successfulIsspLoginAlready = doc.selectFirst("a[aria-label='povratak u sustav']")
            ?.text()?.contains("Povratak u sustav", true) == true
        successfulAaieduLoginAlready = doc.selectFirst("div[class=onscript-msg]")
            ?.text()?.contains("Uspješno ste autenticirani.", true) == true


        if (successfulAaieduLoginAlready) {
            doc.select("input[name=SAMLResponse]").forEach { sAMLResponse = it.attr("value") }
            return NetworkServiceResult.IksicaResult.Success("Success early login to AAIEDU")
        }
        if (successfulIsspLoginAlready) {
            return NetworkServiceResult.IksicaResult.Success("Success early login to ISSP")
        }

        if (!success || body.isEmpty()) {
            return NetworkServiceResult.IksicaResult.Failure(Throwable("Failed to get AuthState"))
        }

        authState = response.request.url.encodedQuery ?: ""// .queryParameter("AuthState") ?: ""
        currentUrl = response.request.url

        return NetworkServiceResult.IksicaResult.Success("Success")
    }

    override suspend fun login(email: String, password: String): NetworkServiceResult.IksicaResult {
        if (successfulAaieduLoginAlready || successfulIsspLoginAlready) {
            successfulAaieduLoginAlready = false
            return NetworkServiceResult.IksicaResult.Success("Success login")
        }

        val response: HttpResponse = client.submitForm(
            url = currentUrl.toString(),
            formParameters = Parameters.build {
                append("username", email)
                append("password", password)
                append("AuthState", authState)
                append("Submit", "")
            })
        val data = response.bodyAsText()
        val doc = Ksoup.parse(data)
        sAMLResponse = doc.select("input[name=SAMLResponse]").attr("value")

        val content = doc.selectFirst("div.onscript-msg")?.text()
        val submit = doc.selectFirst("button[type=submit]")?.text()
        val error = doc.selectFirst("div.error")?.text()

        if (content != null && content.contains("Uspješno", true)
            || submit != null && submit.contains("Nastavak", true)
        ) {
            return NetworkServiceResult.IksicaResult.Success("Success login")
        }

        if (error != null && error.contains("Greška", true)) {
            return NetworkServiceResult.IksicaResult.Failure(Throwable(error))
        }

        return NetworkServiceResult.IksicaResult.Failure(Throwable("Failure login"))
    }

    override suspend fun getAspNetSessionSAML(): NetworkServiceResult.IksicaResult {
        if (successfulIsspLoginAlready) {
            successfulIsspLoginAlready = false
            return NetworkServiceResult.IksicaResult.Success("Success login")
        }
        val response: HttpResponse = client.submitForm(
            url = "https://issp.srce.hr/auth/prijavakorisnika",
            formParameters = Parameters.build {
                append("SAMLResponse", sAMLResponse)
                append("Submit", "")
            })
        val body = response.bodyAsText()

        val error = Ksoup.parse(body).selectFirst(".alert-danger")?.text()

        if (error != null && error.contains("Greška", true)) {
            return NetworkServiceResult.IksicaResult.Failure(Throwable(error.substringAfter("error_outline ")))
        }

        if (!response.status.isSuccess()) {
            return NetworkServiceResult.IksicaResult.Failure(Throwable("Failure getAspNetSessionSAML"))
        }

        return NetworkServiceResult.IksicaResult.Success("Success")
    }
}