package dev.etino.fcshared.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import dev.etino.fcshared.features.attendance.compose.AttendanceCompose
import dev.etino.fcshared.features.attendance.view.AttendanceViewModel
import dev.etino.fcshared.features.home.view.HomeTabCompose
import dev.etino.fcshared.features.home.view.HomeViewModel
import dev.etino.fcshared.features.iksica.compose.IksicaCompose
import dev.etino.fcshared.features.iksica.view.IksicaViewModel
import dev.etino.fcshared.features.login.compose.LoginCompose
import dev.etino.fcshared.features.login.view.LoginViewModel
import dev.etino.fcshared.features.menza.view.MenzaViewModel
import dev.etino.fcshared.features.settings.SettingsCompose
import dev.etino.fcshared.features.settings.SettingsViewModel
import dev.etino.fcshared.features.studomat.compose.StudomatCompose
import dev.etino.fcshared.features.studomat.view.StudomatViewModel
import dev.etino.fcshared.features.timetable.TimetableViewModel
import dev.etino.fcshared.features.timetable.compose.TimetableCompose
import dev.jordond.connectivity.Connectivity
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
import org.koin.compose.koinInject
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
    val connectivity = koinInject<Connectivity>()
    val internetAvailable =
        connectivity.statusUpdates.collectAsState(Connectivity.Status.Connected(false)).value.isConnected

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
        },
        floatingActionButton = {
            if (!internetAvailable) NoInternetIcon()
        }
    ) { paddingValues ->
        val entryProvider: (NavKey) -> NavEntry<NavKey> = entryProvider {
            entry<Login> {
                val loginViewModel = koinViewModel<LoginViewModel>()

                LaunchedEffect(loginViewModel.loggedIn.collectAsState().value) {
                    if (loginViewModel.loggedIn.value) {
                        navigator.navigate(Home)
                        loginViewModel.clearViewModel()
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
                SettingsCompose(koinViewModel<SettingsViewModel>(), paddingValues, navigator::navigate, navigator::goBack)
            }
        }
        NavDisplay(
            entries = navigationState.toEntries(entryProvider),
            onBack = { navigator.goBack() },
        )
    }
}
