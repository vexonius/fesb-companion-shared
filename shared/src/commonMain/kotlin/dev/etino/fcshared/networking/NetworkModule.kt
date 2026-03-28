package dev.etino.fcshared.networking

import dev.etino.fcshared.attendance.services.AttendanceService
import dev.etino.fcshared.attendance.services.AttendanceServiceInterface
import dev.etino.fcshared.home.services.WeatherService
import dev.etino.fcshared.home.services.WeatherServiceInterface
import dev.etino.fcshared.login.services.UserService
import dev.etino.fcshared.login.services.UserServiceInterface
import dev.etino.fcshared.timetable.TimetableClient
import dev.etino.fcshared.timetable.TimetableClientImpl
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class)
val servicesModule = module {
    single<TimetableClient> { TimetableClientImpl(get()) }
    single<UserServiceInterface> { UserService(get()) }
    single<AttendanceServiceInterface> { AttendanceService(get()) }
    single<WeatherServiceInterface> { WeatherService(get()) }
    single<CustomCookieStorage> { CustomCookieStorage() }
}
