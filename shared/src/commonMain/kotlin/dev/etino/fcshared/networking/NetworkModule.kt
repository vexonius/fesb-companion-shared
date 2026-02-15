package dev.etino.fcshared.networking

import dev.etino.fcshared.CustomCookieStorage
import dev.etino.fcshared.attendance.services.AttendanceService
import dev.etino.fcshared.attendance.services.AttendanceServiceInterface
import dev.etino.fcshared.login.services.UserService
import dev.etino.fcshared.login.services.UserServiceInterface
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.HttpCookies
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class)
val networkModule = module {
    single<HttpClient> { client }
    single<UserServiceInterface> { UserService(get()) }
    single<AttendanceServiceInterface> { AttendanceService(get()) }
}

val client = HttpClient {
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