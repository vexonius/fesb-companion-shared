package dev.etino.fcshared.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import dev.etino.fcshared.screens.attendance.compose.AttendanceCompose
import dev.etino.fcshared.screens.attendance.view.AttendanceViewModel
import dev.etino.fcshared.screens.home.view.HomeTabCompose
import dev.etino.fcshared.screens.home.view.HomeViewModel
import dev.etino.fcshared.screens.login.compose.LoginCompose
import dev.etino.fcshared.screens.login.view.LoginViewModel
import dev.etino.fcshared.screens.menza.view.MenzaViewModel
import dev.etino.fcshared.screens.settings.SettingsCompose
import dev.etino.fcshared.screens.settings.SettingsViewModel
import dev.etino.fcshared.screens.timetable.TimetableViewModel
import dev.etino.fcshared.screens.timetable.compose.TimetableCompose
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.icon_attendance
import fesb_companion_shared.composeapp.generated.resources.icon_home
import fesb_companion_shared.composeapp.generated.resources.icon_iksica
import fesb_companion_shared.composeapp.generated.resources.icon_studomat
import fesb_companion_shared.composeapp.generated.resources.icon_timetable
import fesb_companion_shared.composeapp.generated.resources.tab_attendance
import fesb_companion_shared.composeapp.generated.resources.tab_home
import fesb_companion_shared.composeapp.generated.resources.tab_iksica
import fesb_companion_shared.composeapp.generated.resources.tab_studomat
import fesb_companion_shared.composeapp.generated.resources.tab_timetable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.koin.compose.viewmodel.koinViewModel

data class TopLevelRoute(val nameId: StringResource, val route: NavKey, val iconId: DrawableResource)

val topLevelRoutes = listOf(
    TopLevelRoute(Res.string.tab_iksica, Iksica, Res.drawable.icon_iksica),
    TopLevelRoute(Res.string.tab_attendance, Attendance, Res.drawable.icon_attendance),
    TopLevelRoute(Res.string.tab_home, Home, Res.drawable.icon_home),
    TopLevelRoute(Res.string.tab_timetable, TimeTable, Res.drawable.icon_timetable),
    TopLevelRoute(Res.string.tab_studomat, Studomat, Res.drawable.icon_studomat),
)



@OptIn(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun Application(loggedIn: Boolean) {
    val navigationState = rememberNavigationState(
        startRoute = if (!loggedIn) Login else Home,
        topLevelRoutes = topLevelRoutes.map { it.route }.plus(Login).toSet()
    )

    val navigator = remember { Navigator(navigationState) }

    Scaffold(
        bottomBar = {
            MainBottomBar(
                navigate = navigator::navigate,
                topLevelRoute = navigationState.topLevelRoute,
                topLevelRoutes = topLevelRoutes
            )
        }
    ) { paddingValues ->
        val entryProvider = entryProvider {
            entry<Login> { key ->
                val loginViewModel = koinViewModel<LoginViewModel>()

                LaunchedEffect(loginViewModel.loggedIn.collectAsState().value) {
                    if (loginViewModel.loggedIn.value) {
                        navigator.navigate(Home)
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
            entry<Attendance> {
                AttendanceCompose(koinViewModel<AttendanceViewModel>(), paddingValues)
            }
            entry<Iksica> { key ->
                Scaffold { ihatethis ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(ihatethis),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) { Text(key.toString()) }
                }
            }
            entry<Home> { key ->
                HomeTabCompose(koinViewModel<HomeViewModel>(), koinViewModel<MenzaViewModel>(),paddingValues, navigator::navigate)
            }
            entry<Studomat> { key ->
                Scaffold { ihatethis ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(ihatethis),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) { Text(key.toString()) }
                }
            }
            entry<TimeTable> { key ->
                TimetableCompose(koinViewModel<TimetableViewModel>(), paddingValues)
            }
            entry<Settings> { key ->
                SettingsCompose(koinViewModel<SettingsViewModel>(), paddingValues)
            }
        }
        NavDisplay(
            entries = navigationState.toDecoratedEntries(entryProvider),
            onBack = { navigator.goBack() },
        )
    }
}