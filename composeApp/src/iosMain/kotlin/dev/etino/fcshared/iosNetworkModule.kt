package dev.etino.fcshared

import dev.etino.fcshared.networking.CustomCookieStorage
import dev.jordond.connectivity.Connectivity
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.HttpCookies
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class)
val iosNetworkModule = module {
    single<HttpClient> { provideIosClient(get()) }
    single<HttpClientEngineFactory<*>> { Darwin }
    single<Connectivity> { getConnectivity() }
}

fun provideIosClient(cookieStorage: CustomCookieStorage): HttpClient {
    return HttpClient(Darwin) {
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
}


fun getConnectivity(): Connectivity {
    val connectivity = Connectivity()
    connectivity.start()
    return connectivity
}
