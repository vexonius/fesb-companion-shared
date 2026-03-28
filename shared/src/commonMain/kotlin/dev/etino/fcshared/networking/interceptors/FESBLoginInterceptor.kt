package dev.etino.fcshared.networking.interceptors

import dev.etino.fcshared.login.dao.UserDao
import dev.etino.fcshared.login.services.UserServiceInterface

class FESBLoginInterceptor(
    private val userService: UserServiceInterface,
    private val userDao: UserDao
) {

    suspend fun refreshSession() {
        val user = userDao.getUser()
        userService.loginUser(user.username, user.password)
    }

}