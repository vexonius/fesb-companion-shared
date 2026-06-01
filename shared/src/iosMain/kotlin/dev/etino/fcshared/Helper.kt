package dev.etino.fcshared

import dev.etino.fcshared.featuresCompose.attendance.di.attendanceModule
import dev.etino.fcshared.featuresCompose.home.di.homeModule
import dev.etino.fcshared.featuresCompose.iksica.di.iksicaModule
import dev.etino.fcshared.featuresCompose.login.di.loginModule
import dev.etino.fcshared.featuresCompose.menza.di.menzaModule
import dev.etino.fcshared.featuresCompose.settings.di.settingsModule
import dev.etino.fcshared.featuresCompose.studomat.di.studomatModule
import dev.etino.fcshared.featuresCompose.timetable.di.timetableModule
import dev.etino.fcshared.featuresKotlin.networking.servicesModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            attendanceModule,
            loginModule,
            dbModule,
            iosNetworkModule,
            servicesModule,
            timetableModule,
            homeModule,
            settingsModule,
            menzaModule,
            iksicaModule,
            studomatModule,
        )
    }
}