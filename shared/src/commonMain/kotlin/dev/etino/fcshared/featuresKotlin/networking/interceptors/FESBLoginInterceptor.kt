package dev.etino.fcshared.featuresKotlin.networking.interceptors

import dev.etino.fcshared.featuresKotlin.login.dao.UserDao
import dev.etino.fcshared.featuresKotlin.login.services.UserServiceInterface

class FESBLoginInterceptor(
    private val userService: UserServiceInterface,
    private val userDao: UserDao
) {

    suspend fun refreshSession() {
        val user = userDao.getUser()
        userService.loginUser(user.username, user.password)
    }

}