package dev.etino.fcshared

import platform.UIKit.UIViewController
import androidx.compose.ui.window.ComposeUIViewController
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.etino.fcshared.compose.AppTheme
import dev.etino.fcshared.navigation.Application
import dev.etino.fcshared.features.attendance.di.attendanceModule
import dev.etino.fcshared.features.home.di.homeModule
import dev.etino.fcshared.features.iksica.di.iksicaModule
import dev.etino.fcshared.features.login.di.loginModule
import dev.etino.fcshared.features.menza.di.menzaModule
import dev.etino.fcshared.features.settings.di.settingsModule
import dev.etino.fcshared.features.studomat.di.studomatModule
import dev.etino.fcshared.features.timetable.di.timetableModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin

fun MainViewController(): UIViewController {
    // start Koin only once
    startKoin {
        modules(
            attendanceModule,
            loginModule,
            dbModule,
            networkModule1,
            timetableModule,
            homeModule,
            settingsModule,
            menzaModule,
            iksicaModule,
            studomatModule,
        )
    }

    val datastore: DataStore<Preferences> = getKoin().get<DataStore<Preferences>>()
    val loggedIn : Boolean =  runBlocking{
        datastore.data.map { it[SPKey.LOGGED_IN.key] ?: false }.first()
    }
    return ComposeUIViewController {
        AppTheme {
            Application(loggedIn)
        }
    }
}