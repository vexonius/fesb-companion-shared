package dev.etino.fcshared.networking.cookieStorage

import dev.etino.fcshared.networking.Endpoints
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import io.ktor.util.date.GMTDate

object PortalCookieStorage: CookiesStorage {

    private val defaultStorage: CookiesStorage = AcceptAllCookiesStorage()
    private const val authCookieFESB = "Fesb.AuthCookie"

    override suspend fun get(requestUrl: Url): List<Cookie> {
        val stored = defaultStorage.get(requestUrl)

        return stored
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        defaultStorage.addCookie(requestUrl, cookie)
    }

    override fun close() {
        defaultStorage.close()
    }

    suspend fun isFESBTokenValid(): Boolean {
        val cookies = get(Endpoints.tableOverviewUrl.build())
        val authCookies = cookies
            .filter {
                it.name == authCookieFESB && (it.expires?.timestamp ?: 0) > GMTDate().timestamp
            }

        return authCookies.isNotEmpty()
    }

}