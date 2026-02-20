package dev.etino.fcshared.menza.repository

import dev.etino.fcshared.menza.MenzaResult
import dev.etino.fcshared.menza.models.MenzaLocationType

interface MenzaRepositoryInterface {

    suspend fun fetchMenzaDetails(place: MenzaLocationType, insert: Boolean): MenzaResult

}