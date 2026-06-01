package dev.etino.fcshared.featuresKotlin.menza.repository

import dev.etino.fcshared.featuresKotlin.menza.MenzaResult
import dev.etino.fcshared.featuresKotlin.menza.models.MenzaLocationType

interface MenzaRepositoryInterface {

    suspend fun fetchMenzaDetails(place: MenzaLocationType, insert: Boolean): MenzaResult

}