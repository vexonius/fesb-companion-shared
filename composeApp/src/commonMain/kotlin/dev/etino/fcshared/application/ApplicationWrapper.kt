package dev.etino.fcshared.application

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.etino.fcshared.SPKey
import dev.etino.fcshared.compose.AppTheme
import dev.etino.fcshared.compose.SplashScreen
import dev.etino.fcshared.features.login.compose.LoginCompose
import dev.etino.fcshared.features.login.view.LoginViewModel
import dev.etino.fcshared.navigation.Application
import dev.etino.fcshared.navigation.Login
import dev.etino.fcshared.navigation.Splash
import dev.etino.fcshared.navigation.rememberSharedViewModelStoreNavEntryDecorator
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel


@OptIn(InternalCoroutinesApi::class)
@Composable
fun ApplicationWrapper() {

    val datastore: DataStore<Preferences> = koinInject()
    val backStack = remember { mutableStateListOf<Any>(Splash) }

    fun replaceScreenWith(key: NavKey) {
        backStack.removeLastOrNull()
        backStack.add(key)
    }

    LaunchedEffect(Unit) {
        if (datastore.data.map { it[SPKey.LOGGED_IN.key] ?: false }.first()) {
            replaceScreenWith(Application)
        } else {
            replaceScreenWith(Login)
        }
    }

    AppTheme {
        Surface {
            NavDisplay(
                backStack = backStack,
                onBack = { },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberSharedViewModelStoreNavEntryDecorator()
                ),
                entryProvider = entryProvider {
                    entry<Login> {
                        val loginViewModel: LoginViewModel = koinViewModel()

                        LaunchedEffect(loginViewModel.loggedIn.collectAsState().value) {
                            if (loginViewModel.loggedIn.value) {
                                replaceScreenWith(Application)
                            }
                        }
                        LoginCompose(
                            showLoading = loginViewModel.showLoading,
                            username = loginViewModel.username,
                            password = loginViewModel.password,
                            passwordHidden = loginViewModel.passwordHidden,
                            tryUserLogin = { loginViewModel.tryUserLogin() },
                            showSnackbar = loginViewModel.showSnackbar
                        )
                    }
                    entry<Application> {
                        Application { replaceScreenWith(Login) }
                    }
                    entry<Splash> {
                        SplashScreen()
                    }
                }
            )
        }
    }
}