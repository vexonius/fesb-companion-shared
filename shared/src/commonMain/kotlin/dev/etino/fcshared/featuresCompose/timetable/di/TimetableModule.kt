package dev.etino.fcshared.featuresCompose.timetable.di

import dev.etino.fcshared.featuresCompose.timetable.TimetableViewModel
import dev.etino.fcshared.featuresKotlin.database.AppDatabase
import dev.etino.fcshared.featuresKotlin.timetable.dao.TimeTableDao
import dev.etino.fcshared.featuresKotlin.timetable.repository.TimeTableRepository
import dev.etino.fcshared.featuresKotlin.timetable.repository.interfaces.TimeTableRepositoryInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
val timetableModule = module {
    single<TimeTableDao> { getTimeTableDao(get()) }
    single<TimeTableRepositoryInterface> { TimeTableRepository(get(), get()) }
    viewModel { TimetableViewModel(get(), get(), get()) }
}

fun getTimeTableDao(db: AppDatabase): TimeTableDao {
    return db.timetableDao()
}