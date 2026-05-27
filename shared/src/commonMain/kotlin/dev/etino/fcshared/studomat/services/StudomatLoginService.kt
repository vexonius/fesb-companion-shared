package dev.etino.fcshared.studomat.services

import com.fleeksoft.ksoup.Ksoup
import dev.etino.fcshared.networking.NetworkServiceResult
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Parameters

class StudomatLoginService(private val client: HttpClient) : StudomatLoginServiceInterface {

    private var samlRequest = ""
    private var authState = ""
    private var samlResponseEncrypted = ""
    private var samlResponseDecrypted = ""

    override suspend fun getSamlRequest(): NetworkServiceResult.StudomatResult {

        val response: HttpResponse = client.get("https://www.isvu.hr/studomat/saml2/authenticate/isvu")
        val success = response.status.value in 200..299
        val body = response.bodyAsText()
        val doc = Ksoup.parse(body)

        samlRequest = doc.selectFirst("input[name=SAMLRequest]")?.attr("value").toString()

        return if (success && samlRequest != "") {
            logit("StudomatService", "getSamlRequest: $samlRequest")
            NetworkServiceResult.StudomatResult.Success("SAMLRequest got!")
        } else {
            logit("StudomatService", "getSamlRequest: Couldn't get SAMLRequest!")
            throw Throwable("Couldn't get SAMLRequest!")
        }
    }

    override suspend fun sendSamlResponseToAAIEDU(): NetworkServiceResult.StudomatResult {
        val response: HttpResponse = client.submitForm(
            url = "https://login.aaiedu.hr/isvu/saml2/idp/SSOService.php",
            formParameters = Parameters.build {
                append("SAMLRequest", samlRequest)
            }
        )
        val redirectUrl = Ksoup.parse(response.bodyAsText()).selectFirst("#redirlink")?.attr("href")

        if (redirectUrl != null) {
            val redirectedResponse = client.get(redirectUrl)

            authState = Ksoup.parse(redirectedResponse.bodyAsText()).selectFirst("#redirlink")?.attr("href")
                ?.substringAfter("AuthState=") ?: ""
        }

        return if (authState != "") {
            logit("StudomatService", "sendSamlResponseToAAIEDU: $authState")
            NetworkServiceResult.StudomatResult.Success("SAMLResponse sent to AAIEDU!")
        } else {
            logit("StudomatService", "sendSamlResponseToAAIEDU: Couldn't send SAMLResponse to AAIEDU!")
            throw Throwable("Couldn't send SAMLResponse to AAIEDU!")
        }
    }

    override suspend fun getSamlResponse(
        email: String,
        password: String
    ): NetworkServiceResult.StudomatResult {

        val response: HttpResponse = client.submitForm(
            url = "https://login.aaiedu.hr/sso/module.php/core/loginuserpass?AuthState=$authState",
            formParameters = Parameters.build {
                append("username", email)
                append("password", password)
                append("AuthState", authState)
                append("Submit", "")
            }
        )
        val data = response.bodyAsText()
        val success = response.status.value in 200..299
        val doc = Ksoup.parse(data)

        samlResponseEncrypted = doc.selectFirst("input[name=SAMLResponse]")?.attr("value") ?: ""

        return if (success && samlResponseEncrypted != "") {
            logit("StudomatService", "getSamlResponse: $samlResponseEncrypted")
            println()
            NetworkServiceResult.StudomatResult.Success("SAMLResponse got!")
        } else {
            logit("StudomatService", "getSamlResponse: Couldn't get SAMLResponse!")
            throw Throwable("Couldn't get SAMLResponse!")
        }
    }

    fun logit(a: String, b: String) {
        println("$a         $b")
    }

    override suspend fun sendSAMLToDecrypt(): NetworkServiceResult.StudomatResult {
        val response: HttpResponse = client.submitForm(
            url = "https://login.aaiedu.hr/isvu/module.php/saml/sp/saml2-acs.php/default-sp",
            formParameters = Parameters.build {
                append("SAMLResponse", samlResponseEncrypted)
            }
        )
        val data = response.bodyAsText()
        val success = response.status.value in 200..299
        val doc = Ksoup.parse(data)

        samlResponseDecrypted = doc.selectFirst("input[name=SAMLResponse]")?.attr("value") ?: ""

        return if (success && samlResponseDecrypted != "") {
            logit("StudomatService", "sendSAMLToDecrypt: $samlResponseDecrypted")
            NetworkServiceResult.StudomatResult.Success("SAMLResponse decrypted!")
        } else {
            logit("StudomatService", "sendSAMLToDecrypt: Couldn't decrypt SAMLResponse!")
            throw Throwable("Couldn't decrypt SAMLResponse!")
        }
    }

    override suspend fun sendSAMLToISVU(): NetworkServiceResult.StudomatResult {
        val response: HttpResponse = client.submitForm(
            url = "https://www.isvu.hr/studomat/login/saml2/sso/isvu",
            formParameters = Parameters.build {
                append("SAMLResponse", samlResponseDecrypted)
            }
        )
        val data = response.bodyAsText()
        val success = response.status.value in 200..303

        return if (success) {
            logit("StudomatService", "sendSAMLToISVU: SAMLResponse sent to ISVU!")
            NetworkServiceResult.StudomatResult.Success("SAMLResponse sent to ISVU!")
        } else {
            logit("StudomatService", "sendSAMLToISVU: Couldn't send SAMLResponse to ISVU!")
            throw Throwable("Couldn't send SAMLResponse to ISVU!")
        }
    }
}