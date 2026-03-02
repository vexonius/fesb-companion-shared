package dev.etino.fcshared.networking

import dev.etino.fcshared.iksica.services.IksicaService
import dev.etino.fcshared.studomat.services.StudomatService
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.fillDefaults
import io.ktor.client.plugins.cookies.matches
import io.ktor.http.Cookie
import io.ktor.http.Url
import io.ktor.util.date.GMTDate
import io.ktor.util.date.getTimeMillis
import kotlin.time.Clock


class CustomCookieStorage(
    private val defaultStorage: CookiesStorage = AcceptAllCookiesStorage()
) : CookiesStorage {

    override suspend fun get(requestUrl: Url): List<Cookie> {
        val stored = defaultStorage.get(requestUrl)
        return stored
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        println(cookie)
        val moddedCookie =
            if (!(cookie.name == authCookieISSP || cookie.name == authCookieISVU)) {
                cookie
            } else {
                cookie.copy(
                    maxAge = (expirationTime / 1000).toInt(),
                    expires = GMTDate(Clock.System.now().toEpochMilliseconds() + expirationTime)
                )
            }
        defaultStorage.addCookie(requestUrl, moddedCookie)
    }

    override fun close() {
        defaultStorage.close()
    }

    suspend fun isISVUTokenValid(): Boolean {
        val cookies = get(StudomatService.targetUrl)
        val authCookies = cookies.filter { it.name == authCookieISVU }

        return authCookies.isNotEmpty()
    }

    suspend fun clearISVUCookie() {
        val cookies = get(StudomatService.targetUrl)
        val authCookies = cookies.filter { it.name == authCookieISVU }
        defaultStorage.addCookie(
            StudomatService.targetUrl,
            authCookies.first().copy(
                value = "",
                expires = GMTDate(0)
            )
        )
    }

    suspend fun isISSPTokenValid(): Boolean {
        val cookies = get(IksicaService.targetUrl)
        val authCookies = cookies.filter { it.name == authCookieISSP }

        return authCookies.isNotEmpty()
    }

    suspend fun getISVUCookieForWebView(): Cookie? {
        val cookie = get(StudomatService.targetUrl).find { it.name == authCookieISVU }
        return cookie
    }


    companion object {

        const val authCookieFESB = "Fesb.AuthCookie"
        const val authCookieISSP = ".AspNetCore.saml2"
        const val authCookieISVU = "JSESSIONID"
        const val expirationTime = 3600000L

    }
}
