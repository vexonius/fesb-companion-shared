package dev.etino.fcshared.screens.home.di

import dev.etino.fcshared.home.dao.NoteDao
import dev.etino.fcshared.home.repository.NoteRepository
import dev.etino.fcshared.home.repository.NoteRepositoryInterface
import dev.etino.fcshared.home.repository.WeatherRepository
import dev.etino.fcshared.home.repository.WeatherRepositoryInterface
import dev.etino.fcshared.screens.home.view.HomeViewModel
import dev.etino.fcshared.database.AppDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class)
val homeModule = module {
    single<NoteDao> { getNoteDao(get()) }
    single<NoteRepositoryInterface> { NoteRepository(get()) }
    single<WeatherRepositoryInterface> { WeatherRepository(get()) }
    viewModel { HomeViewModel( get(), get(), get(), get()) }
}


fun getNoteDao(db: AppDatabase): NoteDao {
    return db.noteDao()
}