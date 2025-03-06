package dev.etino.fcshared.di

import com.liftric.kvault.KVault
import dev.etino.fcshared.attendance.client.AttendanceClient
import dev.etino.fcshared.attendance.client.AttendanceClientImpl
import dev.etino.fcshared.attendance.parser.AttendanceParser
import dev.etino.fcshared.attendance.repository.AttendanceRepository
import dev.etino.fcshared.attendance.repository.AttendanceRepositoryImpl
import dev.etino.fcshared.networking.interceptors.LoginInterceptorPlugin
import dev.etino.fcshared.networking.interceptors.LoginInterceptorPluginImpl
import dev.etino.fcshared.timetable.client.TimetableClient
import dev.etino.fcshared.timetable.client.TimetableClientImpl
import dev.etino.fcshared.timetable.parser.TimetableParser
import dev.etino.fcshared.timetable.repository.TimetableRepository
import dev.etino.fcshared.timetable.repository.TimetableRepositoryImpl
import dev.etino.fcshared.user.UserService
import dev.etino.fcshared.user.UserServiceImpl
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun platformModule(vault: KVault) = module {
    single<KVault> { vault }
 }

val attendanceModule: Module = module {

    single { AttendanceParser() }
    single<AttendanceRepository> { AttendanceRepositoryImpl(get(), get()) }
    single<UserService> { UserServiceImpl() }
    single<LoginInterceptorPlugin> { LoginInterceptorPluginImpl(get(), get()) }
    single<AttendanceClient> { AttendanceClientImpl(get()) }

}

val timetableModule = module {

    single { TimetableParser() }
    single<TimetableClient> { TimetableClientImpl() }
    single<TimetableRepository> { TimetableRepositoryImpl(get(), get()) }

}

fun initKoin(vault: KVault) {
    startKoin {
        modules(
            platformModule(vault), attendanceModule, timetableModule)
    }
}