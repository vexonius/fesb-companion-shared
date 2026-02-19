package dev.etino.fcshared.screens.menza.di

import dev.etino.fcshared.menza.repository.CamerasRepository
import dev.etino.fcshared.menza.repository.CamerasRepositoryInterface
import dev.etino.fcshared.menza.service.CamerasService
import dev.etino.fcshared.menza.service.CamerasServiceInterface
import dev.etino.fcshared.menza.repository.MenzaRepository
import dev.etino.fcshared.menza.repository.MenzaRepositoryInterface
import dev.etino.fcshared.menza.service.MenzaService
import dev.etino.fcshared.menza.service.MenzaServiceInterface
import dev.etino.fcshared.screens.menza.view.MenzaViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class)
val menzaModule = module {
    single<MenzaServiceInterface> { MenzaService(get()) }
    single<CamerasServiceInterface> { CamerasService(get()) }
    single<MenzaRepositoryInterface> { MenzaRepository(get()) }
    single<CamerasRepositoryInterface> { CamerasRepository(get()) }
    viewModel { MenzaViewModel(get(), get()) }
}