package dev.etino.fcshared.user

interface UserService {

    @Throws(Exception::class)
    suspend fun login(username: String, password: String): Boolean

}