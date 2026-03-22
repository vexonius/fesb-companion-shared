package dev.etino.fcshared

import platform.UIKit.UIViewController
import androidx.compose.ui.window.ComposeUIViewController
import dev.etino.fcshared.application.ApplicationWrapper
import dev.etino.fcshared.features.attendance.di.attendanceModule
import dev.etino.fcshared.features.home.di.homeModule
import dev.etino.fcshared.features.iksica.di.iksicaModule
import dev.etino.fcshared.features.login.di.loginModule
import dev.etino.fcshared.features.menza.di.menzaModule
import dev.etino.fcshared.features.settings.di.settingsModule
import dev.etino.fcshared.features.studomat.di.studomatModule
import dev.etino.fcshared.features.timetable.di.timetableModule
import org.koin.core.context.startKoin

fun MainViewController(): UIViewController {

    startKoin {
        modules(
            attendanceModule,
            loginModule,
            dbModule,
            iosNetworkModule,
            timetableModule,
            homeModule,
            settingsModule,
            menzaModule,
            iksicaModule,
            studomatModule,
        )
    }

    return ComposeUIViewController {
        ApplicationWrapper()
    }
}