package dev.etino.fcshared.menza.service

import dev.etino.fcshared.networking.NetworkServiceResult

interface MenzaServiceInterface {

    suspend fun fetchMenza(place: String) : NetworkServiceResult.MenzaResult

}