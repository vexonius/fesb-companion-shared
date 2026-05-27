package dev.etino.fcshared.login.user

import dev.etino.fcshared.login.user.models.User
import dev.etino.fcshared.login.user.models.UserRepositoryResult

interface UserRepositoryInterface {

    suspend fun attemptLogin(username: String, password: String): UserRepositoryResult.LoginResult

    suspend fun insertDummyUser()

    suspend fun getCurrentUserName(): String

    suspend fun getCurrentUser(): User

    suspend fun deleteAllUserData()

}
