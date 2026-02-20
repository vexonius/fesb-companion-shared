package dev.etino.fcshared.login.services

import dev.etino.fcshared.networking.NetworkServiceResult

interface UserServiceInterface {

    suspend fun loginUser(username: String, password: String): NetworkServiceResult.LoginResult

}

