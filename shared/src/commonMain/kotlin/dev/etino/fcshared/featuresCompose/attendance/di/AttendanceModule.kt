package dev.etino.fcshared.featuresCompose.attendance.di

import dev.etino.fcshared.featuresCompose.attendance.view.AttendanceViewModel
import dev.etino.fcshared.featuresKotlin.attendance.dao.AttendanceDao
import dev.etino.fcshared.featuresKotlin.attendance.repository.AttendanceRepository
import dev.etino.fcshared.featuresKotlin.attendance.repository.AttendanceRepositoryInterface
import dev.etino.fcshared.featuresKotlin.database.AppDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
val attendanceModule = module {
    single<AttendanceRepositoryInterface> { AttendanceRepository(get(), get()) }
    single<AttendanceDao> { getAttendanceDao(get()) }
    viewModel { AttendanceViewModel(get(), get()) }
}

fun getAttendanceDao(db: AppDatabase): AttendanceDao {
    return db.attendanceDao()
}