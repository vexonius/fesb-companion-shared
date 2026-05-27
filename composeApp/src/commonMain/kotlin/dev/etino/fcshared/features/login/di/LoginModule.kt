package dev.etino.fcshared.features.login.di

import dev.etino.fcshared.database.AppDatabase
import dev.etino.fcshared.features.login.view.LoginViewModel
import dev.etino.fcshared.login.dao.UserDao
import dev.etino.fcshared.login.user.UserRepository
import dev.etino.fcshared.login.user.UserRepositoryInterface
import dev.etino.fcshared.networking.ConnectivityObserver
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class)
val loginModule = module {
    single<UserRepositoryInterface> { UserRepository(get(), get(), get(), get()) }
    single<UserDao> { getUserDao(get()) }
    single<ConnectivityObserver> { ConnectivityObserver(get(), get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
}


fun getUserDao(db: AppDatabase): UserDao {
    return db.userDao()
}
