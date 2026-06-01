package dev.etino.fcshared.featuresKotlin.networking

import androidx.compose.runtime.mutableStateOf
import dev.etino.fcshared.featuresKotlin.iksica.services.IksicaService
import dev.etino.fcshared.featuresKotlin.login.services.UserService
import dev.etino.fcshared.featuresKotlin.studomat.services.StudomatService
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.util.date.GMTDate
import kotlin.time.Clock


class CustomCookieStorage : CookiesStorage {
    private var defaultStorage: CookiesStorage = AcceptAllCookiesStorage()

    private val studomatLoggedIn = mutableStateOf<Long>(0)

    fun clearCookies() {
        defaultStorage.close()
        defaultStorage = AcceptAllCookiesStorage()
    }

    override suspend fun get(requestUrl: Url): List<Cookie> {
        val stored = defaultStorage.get(requestUrl)
        return stored
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        val moddedCookie =
            if (!(cookie.name == authCookieISSP || cookie.name == authCookieISVU)) {
                cookie
            } else {
                if (requestUrl.fullPath == "/studomat/login/saml2/sso/isvu" && cookie.name == authCookieISVU) {
                    studomatLoggedIn.value = GMTDate().timestamp
                }
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

    suspend fun isFESBTokenValid(): Boolean {
        val cookies = get(UserService.targetUrl)
        val authCookies = cookies
            .filter {
                it.name == authCookieFESB &&
                        it.expires != null &&
                        it.expires!! > GMTDate()
            }

        return authCookies.isNotEmpty()
    }

    suspend fun isISVUTokenValid(): Boolean {
        val cookies = get(StudomatService.targetUrl)
        val authCookies = cookies.filter { it.name == authCookieISVU }

        return authCookies.isNotEmpty() && (studomatLoggedIn.value + expirationTime) > GMTDate().timestamp
    }

    suspend fun clearISVUCookie() {
        val cookies = get(StudomatService.targetUrl)
        val authCookies = cookies.filter { it.name == authCookieISVU }
        studomatLoggedIn.value = 0
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
