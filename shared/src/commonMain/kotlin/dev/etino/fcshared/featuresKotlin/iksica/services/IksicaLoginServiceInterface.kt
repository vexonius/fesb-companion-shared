package dev.etino.fcshared.featuresKotlin.iksica.services

import dev.etino.fcshared.featuresKotlin.networking.NetworkServiceResult

interface IksicaLoginServiceInterface {

    suspend fun getAuthState(): NetworkServiceResult.IksicaResult

    suspend fun login(email: String, password: String): NetworkServiceResult.IksicaResult

    suspend fun getAspNetSessionSAML(): NetworkServiceResult.IksicaResult

}