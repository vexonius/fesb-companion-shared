package dev.etino.fcshared.featuresCompose.settings.di

import dev.etino.fcshared.featuresCompose.settings.SettingsViewModel
import dev.etino.fcshared.featuresKotlin.database.AppDatabase
import dev.etino.fcshared.featuresKotlin.login.dao.UserDao
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class)
val settingsModule = module {
    viewModel { SettingsViewModel(get(), get(), get()) }
}


fun getUserDao(db: AppDatabase): UserDao {
    return db.userDao()
}
