package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.etino.fcshared.SPKey
import dev.etino.fcshared.compose.AppTheme
import dev.etino.fcshared.navigation.Application
import dev.etino.fcshared.networking.networkModule
import dev.etino.fcshared.screens.attendance.di.attendanceModule
import dev.etino.fcshared.screens.home.di.homeModule
import dev.etino.fcshared.screens.login.di.loginModule
import dev.etino.fcshared.screens.timetable.di.timetableModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject
import kotlin.getValue

fun main() = application {
    startKoin {
        modules(
            attendanceModule,
            loginModule,
            dbModule,
            networkModule1,
            timetableModule,
            homeModule
        )
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "KotlinProject",
    ) {
        val datastore: DataStore<Preferences> by inject(DataStore::class.java)
        val loggedIn =  runBlocking{
            datastore.data.map { it[SPKey.LOGGED_IN.key] ?: false }.first()
        }
        AppTheme{
            Application(loggedIn)
        }
    }
}