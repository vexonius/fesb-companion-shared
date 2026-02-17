package dev.etino.fcshared.screens.login.view

import android.util.Patterns
import androidx.compose.material3.SnackbarHostState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.etino.fcshared.SPKey
import dev.etino.fcshared.login.user.UserRepositoryInterface
import dev.etino.fcshared.login.user.models.UserRepositoryResult
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@InternalCoroutinesApi
class LoginViewModel(
    private val repository: UserRepositoryInterface,
    private val datastore: DataStore<Preferences>
) : ViewModel() {

    var username = MutableStateFlow("")
    var password = MutableStateFlow("")
    val showLoading = MutableStateFlow(false)
    var passwordHidden = MutableStateFlow(true)

    var firstTimeInApp = MutableStateFlow(false)
        private set

    var loggedIn = MutableStateFlow(false)
        private set

    private val handler = CoroutineExceptionHandler { _, exception ->
        _showSnackbar.update { LoginSnackbar.GENERIC_ERROR }
    }

    private val _showSnackbar = MutableStateFlow<LoginSnackbar?>(null)
    val showSnackbar : StateFlow<LoginSnackbar?> = _showSnackbar

    init{
        checkIfFirstTimeInApp()
        checkIfLoggedIn()
    }

    /*private fun addTestData() {
        val db : AppDatabase by inject(AppDatabase::class.java)
        viewModelScope.launch(Dispatchers.IO + handler) {
            db.studomatDao().insert(studomatSubjectTestData)
            db.studomatDao().insertYears(studomatYearInfoTestData)
            db.timetableDao().insert(eventsTestData)
            db.attendanceDao().insert(attendanceTestData)
            db.iksicaDao().insert(studentDataTestData)
            db.iksicaDao().insert(receiptsTestData)
        }
    }*/

    fun tryUserLogin() {
        var username = username.value?.trim()?.lowercase()
        val password = password.value?.trim()

        /* if (username == "test" && password == "testpassword12421") {
             setTestMode(true)
             viewModelScope.launch(Dispatchers.IO + handler) {
                 repository.insertDummyUser()
             }
             addTestData()
             loggedIn.postValue(Unit)
         }*/

        if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            _showSnackbar.update { LoginSnackbar.EMPTY_CREDENTIALS }
            return
        } else if (isEmailValid(username)) {
            username = username.substringBefore("@")
        }
        showLoading.value = true

        viewModelScope.launch(Dispatchers.IO + handler) {
            when (repository.attemptLogin(username, password)) {
                is UserRepositoryResult.LoginResult.Success -> {
                    loggedIn.update { true }
                }

                is UserRepositoryResult.LoginResult.Failure -> {
                    _showSnackbar.update { LoginSnackbar.INVALID_CREDENTIALS }
                }
            }
            showLoading.value = false
        }
    }

    fun setTestMode(isTestUser: Boolean) {
        viewModelScope.launch {
            datastore.edit { preferences ->
                preferences[SPKey.TEST_MODE.key] = isTestUser
            }
        }
    }

    fun checkIfFirstTimeInApp() {
        viewModelScope.launch {
            firstTimeInApp.value = datastore.data.map { preferences ->
                preferences[SPKey.LOGGED_IN.key] ?: false
            }.first()
            datastore.edit { preferences ->
                preferences[SPKey.FIRST_TIME.key] = false
            }
        }
    }

    fun checkIfLoggedIn() {
        viewModelScope.launch {
            val loggedInDatastore = datastore.data.map { prefs ->
                prefs[SPKey.LOGGED_IN.key] ?: false
            }.first()
            if (loggedInDatastore) {
                loggedIn.value = true
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}


enum class LoginSnackbar{
    INVALID_CREDENTIALS,
    EMPTY_CREDENTIALS,
    GENERIC_ERROR,
}