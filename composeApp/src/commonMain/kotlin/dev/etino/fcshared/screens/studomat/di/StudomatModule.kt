package dev.etino.fcshared.screens.studomat.di

import dev.etino.fcshared.database.AppDatabase
import dev.etino.fcshared.networking.CustomCookieStorage
import dev.etino.fcshared.networking.interceptors.ISVULoginInterceptor
import dev.etino.fcshared.screens.studomat.view.StudomatViewModel
import dev.etino.fcshared.studomat.dao.StudomatDao
import dev.etino.fcshared.studomat.repository.StudomatRepository
import dev.etino.fcshared.studomat.services.StudomatLoginService
import dev.etino.fcshared.studomat.services.StudomatLoginServiceInterface
import dev.etino.fcshared.studomat.services.StudomatService
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.plugin
import kotlinx.coroutines.runBlocking
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val studomatModule = module {
    single<ISVULoginInterceptor> { ISVULoginInterceptor(get(), get()) }
    single<HttpClient>(named("loginclientStudomat")) { provideISVULoginClient(get()) }
    single<StudomatLoginServiceInterface> { StudomatLoginService(get(named("loginclientStudomat"))) }
    single<HttpClient>(named("clientStudomat")) { provideISVUPortalClient(get(), get()) }
    single { StudomatService(get(named("clientStudomat")), get()) }
    single { StudomatRepository(get(), get()) }
    single { getStudomatDao(get()) }
    viewModel { StudomatViewModel(get(),get(),get()) }
}

fun provideISVUPortalClient(
    cookieStorage: CustomCookieStorage,
    isvuLoginInterceptor: ISVULoginInterceptor
): HttpClient {
    val client = HttpClient(CIO) {
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
            runBlocking { isvuLoginInterceptor.refreshSession() }
        }
        execute(request)
    }
    return client
}

fun provideISVULoginClient(cookieStorage: CustomCookieStorage): HttpClient {
    val client = HttpClient(CIO) {
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