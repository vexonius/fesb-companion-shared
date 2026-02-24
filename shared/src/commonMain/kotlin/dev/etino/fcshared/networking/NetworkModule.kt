package dev.etino.fcshared.networking

import dev.etino.fcshared.attendance.services.AttendanceService
import dev.etino.fcshared.attendance.services.AttendanceServiceInterface
import dev.etino.fcshared.home.services.WeatherService
import dev.etino.fcshared.home.services.WeatherServiceInterface
import dev.etino.fcshared.iksica.services.IksicaService
import dev.etino.fcshared.login.services.UserService
import dev.etino.fcshared.login.services.UserServiceInterface
import dev.etino.fcshared.timetable.TimetableClient
import dev.etino.fcshared.timetable.TimetableClientImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class)
val networkModule = module {
    single<CustomCookieStorage> { CustomCookieStorage() }
    single<HttpClient> { provideClient(get()) }
    single<TimetableClient> { TimetableClientImpl(get()) }
    single<UserServiceInterface> { UserService(get()) }
    single<AttendanceServiceInterface> { AttendanceService(get()) }
    single<WeatherServiceInterface> { WeatherService(get()) }
}

fun provideClient(cookieJar: CustomCookieStorage): HttpClient {
    return HttpClient(CIO) {
        expectSuccess = false

        install(HttpRedirect) {
            checkHttpMethod = false
        }
        install(HttpSend) {

        }
        install(HttpCookies) {
            storage = cookieJar
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 10_000
        }
    }
}

class CustomCookieStorage(
    private val defaultStorage: CookiesStorage = AcceptAllCookiesStorage()
) : CookiesStorage {

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
    suspend fun isISSPTokenValid(): Boolean {
        val cookies = get(IksicaService.targetUrl)
        val authCookies = cookies.filter { it.name == authCookieISSP }

        return authCookies.isNotEmpty()
    }
    companion object {

        const val authCookieFESB = "Fesb.AuthCookie"
        const val authCookieISSP = ".AspNetCore.saml2"
        const val authCookieISVU = "JSESSIONID"
        const val expirationTime = 3600000L

    }
}
