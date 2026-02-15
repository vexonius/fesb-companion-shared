package dev.etino.fcshared.screens.attendance.di

import dev.etino.fcshared.attendance.repository.AttendanceRepository
import dev.etino.fcshared.attendance.repository.AttendanceRepositoryInterface
import dev.etino.fcshared.attendance.services.AttendanceService
import dev.etino.fcshared.attendance.services.AttendanceServiceInterface
import dev.etino.fcshared.screens.attendance.view.AttendanceViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
val attendanceModule = module {
    single<AttendanceServiceInterface> { AttendanceService() }
    single<AttendanceRepositoryInterface> { AttendanceRepository(get()) }
    viewModel { AttendanceViewModel(get()) }
}
