package dev.etino.fcshared.user

import dev.etino.fcshared.networking.PortalCookieStorage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.statement.request
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.utils.io.InternalAPI

class UserServiceImpl : UserService {

    private val client = HttpClient {
        followRedirects = false

        install(UserAgent) {
            agent = "Mozilla/5.0 (iPhone; CPU iPhone OS 17_7_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/134.0.6998.33 Mobile/15E148 Safari/604.1"
        }

        install(Logging) {
            level = LogLevel.ALL
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 10_000
        }

        install(HttpCookies) {
            storage = PortalCookieStorage
        }
    }

    @OptIn(InternalAPI::class)
    @Throws(Exception::class)
    override suspend fun login(username: String, password: String): Boolean {
        val response = client.post(loginUrl.buildString()) {
            body = MultiPartFormDataContent(
                formData {
                    append("Username", username)
                    append("Password", password)
                    append("IsRememberMeChecked", "true")
                }
            )
        }

        val responseUrl = response.request.url.toString()

        return responseUrl == targetUrl
    }

    companion object {
        val targetUrl = URLBuilder(protocol = URLProtocol.HTTPS, host = "korisnik.fesb.unist.hr")
            .buildString()

        val loginUrl = URLBuilder(
                protocol = URLProtocol.HTTPS,
                host = "korisnik.fesb.unist.hr",
                pathSegments = listOf("prijava"))
    }

}