package dev.etino.fcshared.features.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.etino.fcshared.login.user.UserRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userRepository: UserRepositoryInterface,
    private val datastore: DataStore<Preferences>,
) : ViewModel() {

    val username: MutableStateFlow<String> = MutableStateFlow("")
    val version: MutableStateFlow<String> = MutableStateFlow("")
    val displayLicences = MutableStateFlow(false)
    val routeToLogin = MutableStateFlow(false)

    init {
        viewModelScope.launch(Dispatchers.Default) {
            username.update { userRepository.getCurrentUserName() }
            //version.update { getBuildVersion() }
        }
    }

    /**
     * Method below will remove all user data from database,
     * which will trigger session delegate flow event and
     * router will route to login screen
     */
    fun logout() {
        viewModelScope.launch(Dispatchers.Default) {
            userRepository.deleteAllUserData()
            routeToLogin.emit(true)
            datastore.edit { it.clear() }
        }
    }

    /*private fun getBuildVersion(): String {
        return try {
            val packageInfo = application.applicationContext.packageManager.getPackageInfo(
                application.applicationContext.packageName,
                0
            )
            packageInfo.versionName ?: "undefined"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "undefined"
        }
    }
*/
    fun displayLicensesDialog() {
        displayLicences.update { true }
    }

    fun hideLicensesDialog() {
        displayLicences.update { false }
    }

    /*fun getSupportEmailModalModel(): EmailModalModel {
        val title = getString(application, R.string.send_mail_using)
        val subject = "${getString(application, R.string.feedback_email_subject)} v${version.value}"

        return EmailModalModel(feedbackRecipientAddress, title, subject, "")
    }

    fun getBugReportEmailModalModel(): EmailModalModel {
        val title = getString(application, R.string.send_mail_using)
        val subject = "${getString(application, R.string.report_bug_email_subject)} v${version.value}"

        return EmailModalModel(feedbackRecipientAddress, title, subject, "")
    }*/

    companion object {
        const val pivacyUrl = "https://privacy.etino.dev"
        const val feedbackRecipientAddress = "support@fesbcompanion.xyz"
    }

}
