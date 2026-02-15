package dev.etino.fcshared.screens.login.view

import android.util.Patterns
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.etino.fcshared.login.user.UserRepositoryInterface
import dev.etino.fcshared.login.user.models.UserRepositoryResult
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class LoginViewModel(
    private val repository: UserRepositoryInterface,
    //private val sharedPreferences: SharedPreferences
) : ViewModel() {

    var username = MutableStateFlow("")
    var password = MutableStateFlow("")
    val showLoading = MutableStateFlow(false)
    val snackbarHostState: SnackbarHostState = SnackbarHostState()
    var passwordHidden = MutableStateFlow(true)

    var firstTimeInApp = MutableStateFlow(false)
        private set

    /*  var loggedIn = SingleLiveEvent<Unit>()
          private set
  */
    private val handler = CoroutineExceptionHandler { _, exception ->
        //showSnackbar(application.getString(R.string.login_error_generic))
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
            //showSnackbar(application.getString(Res.string.login_error_empty_credentials))
            return
        } else if (isEmailValid(username)) {
            username = username.substringBefore("@")
        }
        showLoading.value = true

        viewModelScope.launch(Dispatchers.IO + handler) {
            when (repository.attemptLogin(username, password)) {
                is UserRepositoryResult.LoginResult.Success -> {
                    //loggedIn.postValue(Unit)
                }

                is UserRepositoryResult.LoginResult.Failure -> {
                    //showSnackbar(application.getString(R.string.login_error_invalid_credentials))
                }
            }
            showLoading.value = false
        }
    }

    private fun showSnackbar(message: String) {
        viewModelScope.launch(Dispatchers.Main) {
            snackbarHostState.showSnackbar(message)
        }
    }

    /*  fun setTestMode(isTestUser: Boolean) {
          sharedPreferences[SPKey.TEST_MODE] = isTestUser
      }

      fun checkIfFirstTimeInApp() {
          firstTimeInApp.value = sharedPreferences[SPKey.FIRST_TIME, true]
          sharedPreferences[SPKey.FIRST_TIME] = false
      }

      fun checkIfLoggedIn() {
          if (sharedPreferences[SPKey.LOGGED_IN, false]) {
              loggedIn.value = Unit
          }
      }
  */
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}
