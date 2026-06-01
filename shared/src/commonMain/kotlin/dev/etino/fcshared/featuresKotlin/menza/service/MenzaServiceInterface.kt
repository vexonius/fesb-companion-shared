package dev.etino.fcshared.featuresKotlin.menza.service

import dev.etino.fcshared.featuresKotlin.networking.NetworkServiceResult

interface MenzaServiceInterface {

    suspend fun fetchMenza(place: String): NetworkServiceResult.MenzaResult

}