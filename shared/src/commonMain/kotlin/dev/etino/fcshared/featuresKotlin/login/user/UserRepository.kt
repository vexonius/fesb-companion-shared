package dev.etino.fcshared.featuresKotlin.login.user


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import dev.etino.fcshared.featuresKotlin.SPKey
import dev.etino.fcshared.featuresKotlin.database.AppDatabase
import dev.etino.fcshared.featuresKotlin.login.dao.UserDao
import dev.etino.fcshared.featuresKotlin.login.services.UserServiceInterface
import dev.etino.fcshared.featuresKotlin.login.user.models.User
import dev.etino.fcshared.featuresKotlin.login.user.models.UserRepositoryResult
import dev.etino.fcshared.featuresKotlin.login.user.models.UserRoom
import dev.etino.fcshared.featuresKotlin.networking.NetworkServiceResult

class UserRepository(
    private val userService: UserServiceInterface,
    private val userDao: UserDao,
    private val datastore: DataStore<Preferences>,
    private val appDatabase: AppDatabase,
) : UserRepositoryInterface {

    override suspend fun attemptLogin(username: String, password: String): UserRepositoryResult.LoginResult {
        return when (val result = userService.loginUser(username, password)) {
            is NetworkServiceResult.LoginResult.Success -> {
                val user = result.data
                userDao.insert(UserRoom(user))
                datastore.edit { it[SPKey.LOGGED_IN.key] = true }

                UserRepositoryResult.LoginResult.Success(result.data)
            }

            is NetworkServiceResult.LoginResult.Failure -> {
                UserRepositoryResult.LoginResult.Failure(Throwable("User Login failed!"))
            }
        }
    }

    override suspend fun insertDummyUser() {
        datastore.edit { it[SPKey.LOGGED_IN.key] = true }
        userDao.insert(UserRoom(11, "Test user", "User", "User"))
    }

    override suspend fun getCurrentUserName(): String {
        return userDao.getUser().username
    }

    override suspend fun getCurrentUser(): User {
        return User(userDao.getUser())
    }

    override suspend fun deleteAllUserData() {
        appDatabase.attendanceDao().deleteAll()
        appDatabase.timetableDao().deleteAll()
        appDatabase.noteDao().deleteAll()
        appDatabase.iksicaDao().deleteAllReceipts()
        appDatabase.iksicaDao().deleteStudent()
        appDatabase.studomatDao().deleteAllSubjects()
        appDatabase.studomatDao().deleteYears()
        appDatabase.userDao().deleteAllUserData()
        datastore.edit { it[SPKey.LOGGED_IN.key] = false }
    }
}
