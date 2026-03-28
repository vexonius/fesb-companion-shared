package dev.etino.fcshared

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import dev.etino.fcshared.database.AppDatabase
import dev.etino.fcshared.networking.CustomCookieStorage
import dev.jordond.connectivity.Connectivity
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.HttpCookies
import kotlinx.coroutines.InternalCoroutinesApi
import okio.Path.Companion.toPath
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class)
val androidKoinModuleDB = module {
    factory<AppDatabase> { getRoomDatabase(get()) }
    single<DataStore<Preferences>> {
        createDataStore {
            androidContext().filesDir.resolve(dataStoreFileName).absolutePath
        }
    }

    single<Connectivity> { getConnectivity() }
    single<HttpClient> { provideAndroidClient(get()) }
    single<HttpClientEngineFactory<*>> { CIO }
}

fun provideAndroidClient(cookieJar: CustomCookieStorage): HttpClient {
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
            requestTimeoutMillis = 30_000
        }
    }
}

fun getRoomDatabase(context: Context): AppDatabase {
    val appContext = context.applicationContext
    return Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        DATABASE_NAME
    ).build()
}


const val DATABASE_NAME = "fesbCompanionIosDatabase"

/**
 *   Gets the singleton DataStore instance, creating it if necessary.
 */
fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )

internal const val dataStoreFileName = "FesbCompanion.preferences_pb"

fun getConnectivity(): Connectivity {
    val connectivity = Connectivity()
    connectivity.start()
    return connectivity
}