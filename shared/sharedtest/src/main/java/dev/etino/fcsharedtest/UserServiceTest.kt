package dev.etino.fcsharedtest

import dev.etino.fcshared.user.UserServiceImpl
import kotlinx.coroutines.runBlocking
import org.testng.annotations.Test

@Test
class UserServiceTest {

    fun testUserLogin() {
        runBlocking {
            val userService = UserServiceImpl()
            val result = userService.login("", "")

            assert(result)
        }
    }

}