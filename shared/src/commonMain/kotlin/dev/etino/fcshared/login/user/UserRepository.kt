package dev.etino.fcshared.login.user


import dev.etino.fcshared.login.user.models.User
import dev.etino.fcshared.login.user.models.UserRepositoryResult
import dev.etino.fcshared.login.user.models.UserRoom
import dev.etino.fcshared.login.dao.UserDao
import dev.etino.fcshared.networking.NetworkServiceResult
import dev.etino.fcshared.database.AppDatabase
import dev.etino.fcshared.login.services.UserServiceInterface

class UserRepository(
    private val userService: UserServiceInterface,
    private val userDao: UserDao,
    //private val sharedPreferences: SharedPreferences,
    //private val sessionDelegate: SessionDelegateInterface,
    private val appDatabase: AppDatabase,
) : UserRepositoryInterface {

    override suspend fun attemptLogin(username: String, password: String): UserRepositoryResult.LoginResult {
        return when (val result = userService.loginUser(username, password)) {
            is NetworkServiceResult.LoginResult.Success -> {
                val user = result.data
                userDao.insert(UserRoom(user))
                //sharedPreferences[SPKey.LOGGED_IN] = true

                UserRepositoryResult.LoginResult.Success(result.data)
            }

            is NetworkServiceResult.LoginResult.Failure -> {
                UserRepositoryResult.LoginResult.Failure(Throwable("User Login failed!"))
            }
        }
    }
    override suspend fun insertDummyUser() {
        //sharedPreferences[SPKey.LOGGED_IN] = true
        userDao.insert(UserRoom(11, "Test user", "User", "User"))
    }

    override suspend fun getCurrentUserName(): String {
        return userDao.getUser().username
    }

    override suspend fun getCurrentUser(): User {
        return User(userDao.getUser())
    }

    override suspend fun deleteAllUserData() {
        //sessionDelegate.clearSession()
        //appDatabase.clearAllTables()
        //sharedPreferences[SPKey.LOGGED_IN] = false
    }
}
