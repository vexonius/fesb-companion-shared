package dev.etino.fcshared.screens.settings.di

import dev.etino.fcshared.database.AppDatabase
import dev.etino.fcshared.login.dao.UserDao
import dev.etino.fcshared.screens.settings.SettingsViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class)
val settingsModule = module {
    viewModel { SettingsViewModel( get(), get()) }
}


fun getUserDao(db: AppDatabase): UserDao {
    return db.userDao()
}
