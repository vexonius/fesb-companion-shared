package dev.etino.fcshared.networking

import dev.etino.fcshared.attendance.services.AttendanceService
import dev.etino.fcshared.attendance.services.AttendanceServiceInterface
import dev.etino.fcshared.home.services.WeatherService
import dev.etino.fcshared.home.services.WeatherServiceInterface
import dev.etino.fcshared.login.services.UserService
import dev.etino.fcshared.login.services.UserServiceInterface
import dev.etino.fcshared.networking.interceptors.FESBLoginInterceptor
import dev.etino.fcshared.timetable.TimetableClient
import dev.etino.fcshared.timetable.TimetableClientImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.plugin
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.core.qualifier.named
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class)
val servicesModule = module {
    single<TimetableClient> { TimetableClientImpl(get()) }
    single<UserServiceInterface> { UserService(get()) }
    single<AttendanceServiceInterface> { AttendanceService(get(named("FESBPortalClient"))) }
    single<WeatherServiceInterface> { WeatherService(get()) }
    single<CustomCookieStorage> { CustomCookieStorage() }
    single<HttpClient>(named("FESBPortalClient")) { provideFESBClient(get(), get(), get()) }
}


fun provideFESBClient(
    cookieStorage: CustomCookieStorage,
    fesbLoginInterceptor: FESBLoginInterceptor,
    engineFactory: HttpClientEngineFactory<*>
): HttpClient {
    val client = HttpClient(engineFactory) {
        expectSuccess = false

        install(HttpRedirect) {
            checkHttpMethod = false
        }
        install(HttpSend) {

        }
        install(HttpCookies) {
            storage = cookieStorage
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 10_000
        }
    }
    client.plugin(HttpSend).intercept { request ->

        if (!cookieStorage.isFESBTokenValid() && request.url.host.contains("fesb.unist.hr")) {
            fesbLoginInterceptor.refreshSession()
        }

        execute(request)
    }
    return client
}
