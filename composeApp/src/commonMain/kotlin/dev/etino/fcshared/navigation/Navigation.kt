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
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import dev.etino.fcshared.screens.attendance.compose.AttendanceCompose
import dev.etino.fcshared.screens.attendance.view.AttendanceViewModel
import dev.etino.fcshared.screens.home.view.HomeTabCompose
import dev.etino.fcshared.screens.home.view.HomeViewModel
import dev.etino.fcshared.screens.iksica.compose.IksicaCompose
import dev.etino.fcshared.screens.iksica.view.IksicaViewModel
import dev.etino.fcshared.screens.login.compose.LoginCompose
import dev.etino.fcshared.screens.login.view.LoginViewModel
import dev.etino.fcshared.screens.menza.view.MenzaViewModel
import dev.etino.fcshared.screens.settings.SettingsCompose
import dev.etino.fcshared.screens.settings.SettingsViewModel
import dev.etino.fcshared.screens.studomat.compose.StudomatCompose
import dev.etino.fcshared.screens.studomat.view.StudomatViewModel
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
    val timetableViewModel = koinViewModel<TimetableViewModel>()
    val iksicaViewModel: IksicaViewModel = koinViewModel()
    val homeViewModel: HomeViewModel = koinViewModel()
    val attendanceViewModel: AttendanceViewModel = koinViewModel()
    val studomatViewModel: StudomatViewModel = koinViewModel()

    Scaffold(
        bottomBar = {
            MainBottomBar(
                navigate = navigator::navigate,
                topLevelRoute = navigationState.topLevelRoute,
                topLevelRoutes = topLevelRoutes,
                timetableViewModel = timetableViewModel,
            )
        }
    ) { paddingValues ->
        val entryProvider:(NavKey) -> NavEntry<NavKey> = entryProvider {
            entry<Login> {
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
                AttendanceCompose(attendanceViewModel, paddingValues)
            }
            entry<Iksica> {
                IksicaCompose(iksicaViewModel, paddingValues)
            }
            entry<Home> {
                HomeTabCompose(
                    homeViewModel,
                    koinViewModel<MenzaViewModel>(),
                    paddingValues,
                    navigator::navigate
                )
            }
            entry<Studomat> {
                StudomatCompose(studomatViewModel, paddingValues)
            }
            entry<TimeTable> {
                TimetableCompose(timetableViewModel, paddingValues)
            }
            entry<Settings> {
                SettingsCompose(koinViewModel<SettingsViewModel>(), paddingValues, navigator::navigate)
            }
        }
        NavDisplay(
            entries = navigationState.toEntries(entryProvider),
            onBack = { navigator.goBack() },
        )
    }
}
