package dev.etino.fcshared.featuresCompose.iksica.di

import dev.etino.fcshared.KoinNames
import dev.etino.fcshared.featuresCompose.iksica.view.IksicaViewModel
import dev.etino.fcshared.featuresKotlin.database.AppDatabase
import dev.etino.fcshared.featuresKotlin.iksica.dao.IksicaDao
import dev.etino.fcshared.featuresKotlin.iksica.repository.IksicaRepository
import dev.etino.fcshared.featuresKotlin.iksica.repository.IksicaRepositoryInterface
import dev.etino.fcshared.featuresKotlin.iksica.services.IksicaLoginService
import dev.etino.fcshared.featuresKotlin.iksica.services.IksicaLoginServiceInterface
import dev.etino.fcshared.featuresKotlin.iksica.services.IksicaService
import dev.etino.fcshared.featuresKotlin.iksica.services.IksicaServiceInterface
import dev.etino.fcshared.featuresKotlin.networking.CustomCookieStorage
import dev.etino.fcshared.featuresKotlin.networking.interceptors.ISSPLoginInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.plugin
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class)
val iksicaModule = module {
    single<ISSPLoginInterceptor> { ISSPLoginInterceptor(get(), get()) }
    single<IksicaLoginServiceInterface> { IksicaLoginService(get(), null, "", "") }
    single<HttpClient>(named(KoinNames.ISSPPORTALCLIENT)) { provideISSPPortalClient(get(), get(), get()) }
    single<IksicaServiceInterface> { IksicaService(get(named(KoinNames.ISSPPORTALCLIENT))) }
    single<IksicaRepositoryInterface> { IksicaRepository(get(), get()) }
    single<IksicaDao> { getIksicaDao(get()) }
    viewModel { IksicaViewModel(get(), get()) }
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
            isspLoginInterceptor.refreshSession()
        }
        execute(request)
    }
    return client
}


fun getIksicaDao(db: AppDatabase): IksicaDao {
    return db.iksicaDao()
}