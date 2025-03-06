package dev.etino.fcshared.di

import com.liftric.kvault.KVault
import dev.etino.fcshared.attendance.repository.AttendanceRepository
import dev.etino.fcshared.timetable.repository.TimetableRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object DependenciesProvider {

    fun provideAttendanceRepository(): AttendanceRepository = getKoinInstance<AttendanceRepository>()
    fun provideTimetableRepository(): TimetableRepository = getKoinInstance<TimetableRepository>()
    fun provideKVault(): KVault = getKoinInstance<KVault>()

}

inline fun <reified T> getKoinInstance(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}