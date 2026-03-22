package dev.etino.fcshared

import dev.etino.fcshared.attendance.services.AttendanceService
import dev.etino.fcshared.attendance.services.AttendanceServiceInterface
import dev.etino.fcshared.home.services.WeatherService
import dev.etino.fcshared.home.services.WeatherServiceInterface
import dev.etino.fcshared.login.services.UserService
import dev.etino.fcshared.login.services.UserServiceInterface
import dev.etino.fcshared.networking.CustomCookieStorage
import dev.etino.fcshared.timetable.TimetableClient
import dev.etino.fcshared.timetable.TimetableClientImpl
import dev.jordond.connectivity.Connectivity
import dev.jordond.connectivity.ConnectivityProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.HttpCookies
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.koin.dsl.module
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

@OptIn(InternalCoroutinesApi::class)
val jvmNetworkModule = module {
    single<CustomCookieStorage> { CustomCookieStorage() }
    single<HttpClient> { client }
    single<TimetableClient> { TimetableClientImpl(get()) }
    single<UserServiceInterface> { UserService(get()) }
    single<AttendanceServiceInterface> { AttendanceService(get()) }
    single<WeatherServiceInterface> { WeatherService(get()) }
    single<Connectivity> { getConnectivity() }
}

val client = HttpClient(CIO) {
    expectSuccess = false
    engine {
        https {
            trustManager = object : X509TrustManager {
                override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

                override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

                override fun getAcceptedIssuers(): Array<X509Certificate>? = null
            }
        }
    }

    install(HttpRedirect) {
        checkHttpMethod = false
    }
    install(HttpSend) {

    }
    install(HttpCookies) {
        storage = CustomCookieStorage()
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 10_000
    }
}

fun getConnectivity(): Connectivity {
    val connectivity = Connectivity(
        ConnectivityProvider(
            flow { emit(Connectivity.Status.Connected(false)) }

        ),
        options = { autoStart = true }
    )

    return connectivity
}
