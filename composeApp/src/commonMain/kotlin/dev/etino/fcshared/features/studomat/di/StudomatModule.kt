package dev.etino.fcshared.features.studomat.di

import dev.etino.fcshared.KoinNames
import dev.etino.fcshared.database.AppDatabase
import dev.etino.fcshared.features.studomat.view.StudomatViewModel
import dev.etino.fcshared.networking.CustomCookieStorage
import dev.etino.fcshared.networking.interceptors.ISVULoginInterceptor
import dev.etino.fcshared.studomat.dao.StudomatDao
import dev.etino.fcshared.studomat.repository.StudomatRepository
import dev.etino.fcshared.studomat.services.StudomatLoginService
import dev.etino.fcshared.studomat.services.StudomatLoginServiceInterface
import dev.etino.fcshared.studomat.services.StudomatService
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.plugin
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val studomatModule = module {
    single<ISVULoginInterceptor> { ISVULoginInterceptor(get(), get()) }
    single<HttpClient>(named(KoinNames.LOGINCLIENTSTUDOMAT)) { provideISVULoginClient(get(), get()) }
    single<StudomatLoginServiceInterface> { StudomatLoginService(get(named(KoinNames.LOGINCLIENTSTUDOMAT))) }
    single<HttpClient>(named(KoinNames.CLIENTSTUDOMAT)) { provideISVUPortalClient(get(), get(), get()) }
    single { StudomatService(get(named(KoinNames.CLIENTSTUDOMAT)), get()) }
    single { StudomatRepository(get(), get()) }
    single { getStudomatDao(get()) }
    viewModel { StudomatViewModel(get(), get(), get()) }
}

fun provideISVUPortalClient(
    cookieStorage: CustomCookieStorage,
    isvuLoginInterceptor: ISVULoginInterceptor,
    engineFactory: HttpClientEngineFactory<*>
): HttpClient {
    val client = HttpClient(engineFactory) {
        expectSuccess = false
        install(HttpRedirect) {
            checkHttpMethod = false
        }
        install(HttpCookies) {
            storage = cookieStorage
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
        }
    }
    client.plugin(HttpSend).intercept { request ->

        if (!cookieStorage.isISVUTokenValid()) {
            isvuLoginInterceptor.refreshSession()
        }
        execute(request)
    }
    return client
}

fun provideISVULoginClient(
    cookieStorage: CustomCookieStorage,
    engineFactory: HttpClientEngineFactory<*>
): HttpClient {
    val client = HttpClient(engineFactory) {
        expectSuccess = false
        followRedirects = false
        install(HttpCookies) {
            storage = cookieStorage
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
        }
    }
    return client
}


fun getStudomatDao(db: AppDatabase): StudomatDao {
    return db.studomatDao()
}