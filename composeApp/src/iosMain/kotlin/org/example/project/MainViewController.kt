package org.example.project

import platform.UIKit.UIViewController
import androidx.compose.ui.window.ComposeUIViewController

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
        )
    }

    val datastore: DataStore<Preferences> by inject(DataStore::class.java)
    val loggedIn =  runBlocking{
        datastore.data.map { it[SPKey.LOGGED_IN.key] ?: false }.first()
    }
    return ComposeUIViewController {
        AppTheme {
            Application(loggedIn)
        }
    }
}