package dev.etino.fcshared.featuresCompose.menza.di

import dev.etino.fcshared.featuresKotlin.menza.repository.CamerasRepository
import dev.etino.fcshared.featuresKotlin.menza.repository.CamerasRepositoryInterface
import dev.etino.fcshared.featuresKotlin.menza.service.CamerasService
import dev.etino.fcshared.featuresKotlin.menza.service.CamerasServiceInterface
import dev.etino.fcshared.featuresKotlin.menza.repository.MenzaRepository
import dev.etino.fcshared.featuresKotlin.menza.repository.MenzaRepositoryInterface
import dev.etino.fcshared.featuresKotlin.menza.service.MenzaService
import dev.etino.fcshared.featuresKotlin.menza.service.MenzaServiceInterface
import dev.etino.fcshared.featuresCompose.menza.view.MenzaViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@OptIn(InternalCoroutinesApi::class)
val menzaModule = module {
    single<MenzaServiceInterface> { MenzaService(get()) }
    single<CamerasServiceInterface> { CamerasService(get()) }
    single<MenzaRepositoryInterface> { MenzaRepository(get()) }
    single<CamerasRepositoryInterface> { CamerasRepository(get()) }
    viewModel { MenzaViewModel(get(), get(), get()) }
}