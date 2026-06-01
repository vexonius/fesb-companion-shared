package dev.etino.fcshared.featuresCompose.home.di

import dev.etino.fcshared.featuresCompose.home.view.HomeViewModel
import dev.etino.fcshared.featuresKotlin.database.AppDatabase
import dev.etino.fcshared.featuresKotlin.home.dao.NoteDao
import dev.etino.fcshared.featuresKotlin.home.repository.NoteRepository
import dev.etino.fcshared.featuresKotlin.home.repository.NoteRepositoryInterface
import dev.etino.fcshared.featuresKotlin.home.repository.WeatherRepository
import dev.etino.fcshared.featuresKotlin.home.repository.WeatherRepositoryInterface
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class)
val homeModule = module {
    single<NoteDao> { getNoteDao(get()) }
    single<NoteRepositoryInterface> { NoteRepository(get()) }
    single<WeatherRepositoryInterface> { WeatherRepository(get()) }
    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
}


fun getNoteDao(db: AppDatabase): NoteDao {
    return db.noteDao()
}
