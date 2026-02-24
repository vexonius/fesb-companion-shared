package dev.etino.fcshared.iksica.services

import dev.etino.fcshared.networking.NetworkServiceResult

interface IksicaLoginServiceInterface {

    suspend fun getAuthState(): NetworkServiceResult.IksicaResult

    suspend fun login(email: String, password: String): NetworkServiceResult.IksicaResult

    suspend fun getAspNetSessionSAML(): NetworkServiceResult.IksicaResult

}