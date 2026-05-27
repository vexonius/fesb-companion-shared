package dev.etino.fcshared

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.etino.fcshared.application.ApplicationWrapper
import dev.etino.fcshared.features.attendance.di.attendanceModule
import dev.etino.fcshared.features.home.di.homeModule
import dev.etino.fcshared.features.iksica.di.iksicaModule
import dev.etino.fcshared.features.login.di.loginModule
import dev.etino.fcshared.features.menza.di.menzaModule
import dev.etino.fcshared.features.settings.di.settingsModule
import dev.etino.fcshared.features.studomat.di.studomatModule
import dev.etino.fcshared.features.timetable.di.timetableModule
import dev.etino.fcshared.networking.servicesModule
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        modules(
            attendanceModule,
            loginModule,
            dbModule,
            jvmNetworkModule,
            servicesModule,
            timetableModule,
            homeModule,
            settingsModule,
            menzaModule,
            iksicaModule,
            studomatModule,
        )
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Fesb Companion",
    ) {
        ApplicationWrapper()
    }
}