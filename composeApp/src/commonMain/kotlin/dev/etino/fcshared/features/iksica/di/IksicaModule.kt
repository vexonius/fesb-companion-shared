package dev.etino.fcshared.features.iksica.di

import dev.etino.fcshared.networking.interceptors.ISSPLoginInterceptor
import dev.etino.fcshared.database.AppDatabase
import dev.etino.fcshared.iksica.dao.IksicaDao
import dev.etino.fcshared.iksica.repository.IksicaRepository
import dev.etino.fcshared.iksica.repository.IksicaRepositoryInterface
import dev.etino.fcshared.iksica.services.IksicaLoginService
import dev.etino.fcshared.iksica.services.IksicaLoginServiceInterface
import dev.etino.fcshared.iksica.services.IksicaService
import dev.etino.fcshared.iksica.services.IksicaServiceInterface
import dev.etino.fcshared.networking.CustomCookieStorage
import dev.etino.fcshared.features.iksica.view.IksicaViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.plugin
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class)
val iksicaModule = module {
    single<ISSPLoginInterceptor> { ISSPLoginInterceptor(get(), get()) }
    single<IksicaLoginServiceInterface> { IksicaLoginService(get(), null, "", "") }
    single<HttpClient>(named("ISSPPortalClient")) { provideISSPPortalClient(get(), get(), get()) }
    single<IksicaServiceInterface> { IksicaService(get(named("ISSPPortalClient"))) }
    single<IksicaRepositoryInterface> { IksicaRepository(get(), get()) }
    single<IksicaDao> { getIksicaDao(get()) }
    viewModel { IksicaViewModel(get(),get()) }
}

fun provideISSPPortalClient(
    cookieStorage: CustomCookieStorage,
    isspLoginInterceptor: ISSPLoginInterceptor,
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

        if (!cookieStorage.isISSPTokenValid() && request.url.pathSegments.contains("student")) {
            runBlocking { isspLoginInterceptor.refreshSession() }
        }
        execute(request)
    }
    return client
}


fun getIksicaDao(db: AppDatabase): IksicaDao {
    return db.iksicaDao()
}