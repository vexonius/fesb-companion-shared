package dev.etino.fcshared.featuresKotlin.studomat.services

import dev.etino.fcshared.featuresKotlin.networking.NetworkServiceResult

interface StudomatLoginServiceInterface {

    suspend fun getSamlRequest(): NetworkServiceResult.StudomatResult

    suspend fun sendSamlResponseToAAIEDU(): NetworkServiceResult.StudomatResult

    suspend fun getSamlResponse(email: String, password: String): NetworkServiceResult.StudomatResult

    suspend fun sendSAMLToDecrypt(): NetworkServiceResult.StudomatResult

    suspend fun sendSAMLToISVU(): NetworkServiceResult.StudomatResult

}