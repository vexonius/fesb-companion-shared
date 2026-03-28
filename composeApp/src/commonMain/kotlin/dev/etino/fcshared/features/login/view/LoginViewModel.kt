package dev.etino.fcshared.features.login.view

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.etino.fcshared.SPKey
import dev.etino.fcshared.database.AppDatabase
import dev.etino.fcshared.features.login.*
import dev.etino.fcshared.login.user.UserRepositoryInterface
import dev.etino.fcshared.login.user.models.UserRepositoryResult
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.login_error_empty_credentials
import fesb_companion_shared.composeapp.generated.resources.login_error_generic
import fesb_companion_shared.composeapp.generated.resources.login_error_invalid_credentials
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

@InternalCoroutinesApi
class LoginViewModel(
    private val repository: UserRepositoryInterface,
    private val datastore: DataStore<Preferences>,
    private val db : AppDatabase,
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
        _showSnackbar.update { Res.string.login_error_generic }
    }

    private val _showSnackbar = MutableStateFlow<StringResource?>(null)
    val showSnackbar : StateFlow<StringResource?> = _showSnackbar

    init{
        checkIfFirstTimeInApp()
        checkIfLoggedIn()
    }

    private fun addTestData() {
        viewModelScope.launch(Dispatchers.Default + handler) {
            db.studomatDao().insert(studomatSubjectTestData)
            db.studomatDao().insertYears(studomatYearInfoTestData)
            db.timetableDao().insert(eventsTestData)
            db.attendanceDao().insert(attendanceTestData)
            db.iksicaDao().insert(studentDataTestData)
            db.iksicaDao().insert(receiptsTestData)
        }
    }

    fun tryUserLogin() {
        var username = username.value.trim().lowercase()
        val password = password.value.trim()

        if (username == "test" && password == "testpassword12421") {
             setTestMode(true)
             viewModelScope.launch(Dispatchers.Default + handler) {
                 repository.insertDummyUser()
             }
             addTestData()
             loggedIn.value = true
        }

        if (username.isEmpty() || password.isEmpty()) {
            _showSnackbar.update { Res.string.login_error_empty_credentials }
            return
        } else if (isEmailValid(username)) {
            username = username.substringBefore("@")
        }
        showLoading.value = true

        viewModelScope.launch(Dispatchers.Default + handler) {
            when (repository.attemptLogin(username, password)) {
                is UserRepositoryResult.LoginResult.Success -> {
                    loggedIn.update { true }
                }

                is UserRepositoryResult.LoginResult.Failure -> {
                    _showSnackbar.update { Res.string.login_error_invalid_credentials }
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

    fun clearViewModel() {
        loggedIn.value = false
        username.value = ""
        password.value = ""
        showLoading.value = false
        passwordHidden.value = true
        firstTimeInApp.value = false
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


    fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        )
        return emailRegex.matches(email)
    }

}
