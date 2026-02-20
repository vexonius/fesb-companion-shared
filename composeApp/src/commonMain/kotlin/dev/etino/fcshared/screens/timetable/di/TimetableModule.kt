package dev.etino.fcshared.screens.timetable.di

import dev.etino.fcshared.timetable.dao.TimeTableDao
import dev.etino.fcshared.timetable.repository.TimeTableRepository
import dev.etino.fcshared.timetable.repository.interfaces.TimeTableRepositoryInterface
import dev.etino.fcshared.database.AppDatabase
import dev.etino.fcshared.screens.timetable.TimetableViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
val timetableModule = module {
    single<TimeTableDao> { getTimeTableDao(get()) }
    single<TimeTableRepositoryInterface> { TimeTableRepository(get(), get()) }
    viewModel { TimetableViewModel(get(), get()) }
}

fun getTimeTableDao(db: AppDatabase): TimeTableDao {
    return db.timetableDao()
}