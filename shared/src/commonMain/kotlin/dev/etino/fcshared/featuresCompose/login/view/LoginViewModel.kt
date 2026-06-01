package dev.etino.fcshared.featuresCompose.login.view

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.etino.fcshared.featuresKotlin.SPKey
import dev.etino.fcshared.featuresKotlin.database.AppDatabase
import dev.etino.fcshared.featuresCompose.login.attendanceTestData
import dev.etino.fcshared.featuresCompose.login.eventsTestData
import dev.etino.fcshared.featuresCompose.login.receiptsTestData
import dev.etino.fcshared.featuresCompose.login.studentDataTestData
import dev.etino.fcshared.featuresCompose.login.studomatSubjectTestData
import dev.etino.fcshared.featuresCompose.login.studomatYearInfoTestData
import dev.etino.fcshared.featuresKotlin.login.user.UserRepositoryInterface
import dev.etino.fcshared.featuresKotlin.login.user.models.UserRepositoryResult
import fesb_companion_shared.shared.generated.resources.Res
import fesb_companion_shared.shared.generated.resources.login_error_empty_credentials
import fesb_companion_shared.shared.generated.resources.login_error_generic
import fesb_companion_shared.shared.generated.resources.login_error_invalid_credentials
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
        showMessage(Res.string.login_error_generic)
        if (showLoading.value) {
            showLoading.value = false
        }
    }

    private val _showSnackbar = MutableSharedFlow<StringResource>()
    val showSnackbar = _showSnackbar.asSharedFlow()

    fun showMessage(resId: StringResource) {
        viewModelScope.launch {
            _showSnackbar.emit(resId)
        }
    }

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
            showMessage(Res.string.login_error_empty_credentials)
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
                    showMessage(Res.string.login_error_invalid_credentials)
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


    fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        )
        return emailRegex.matches(email)
    }

}
