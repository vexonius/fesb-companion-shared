package dev.etino.fcshared.featuresKotlin.login.services

import dev.etino.fcshared.featuresKotlin.networking.NetworkServiceResult

interface UserServiceInterface {

    suspend fun loginUser(username: String, password: String): NetworkServiceResult.LoginResult

}

