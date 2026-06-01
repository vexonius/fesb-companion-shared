package dev.etino.fcshared.application

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import dev.etino.fcshared.featuresCompose.attendance.compose.AttendanceCompose
import dev.etino.fcshared.featuresCompose.attendance.view.AttendanceViewModel
import dev.etino.fcshared.featuresCompose.home.view.HomeTabCompose
import dev.etino.fcshared.featuresCompose.home.view.HomeViewModel
import dev.etino.fcshared.featuresCompose.iksica.compose.IksicaCompose
import dev.etino.fcshared.featuresCompose.iksica.view.IksicaViewModel
import dev.etino.fcshared.featuresCompose.settings.SettingsCompose
import dev.etino.fcshared.featuresCompose.settings.SettingsViewModel
import dev.etino.fcshared.featuresCompose.studomat.compose.StudomatCompose
import dev.etino.fcshared.featuresCompose.studomat.view.StudomatViewModel
import dev.etino.fcshared.featuresCompose.timetable.TimetableViewModel
import dev.etino.fcshared.featuresCompose.timetable.compose.TimetableCompose
import dev.etino.fcshared.navigation.Attendance
import dev.etino.fcshared.navigation.Home
import dev.etino.fcshared.navigation.Iksica
import dev.etino.fcshared.navigation.MainBottomBar
import dev.etino.fcshared.navigation.Navigator
import dev.etino.fcshared.navigation.NoInternetIcon
import dev.etino.fcshared.navigation.Settings
import dev.etino.fcshared.navigation.Studomat
import dev.etino.fcshared.navigation.TimeTable
import dev.etino.fcshared.navigation.rememberNavigationState
import dev.etino.fcshared.navigation.toEntries
import dev.etino.fcshared.navigation.topLevelRoutes
import dev.etino.fcshared.networking.ConnectivityObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel


@OptIn(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun Application(routeToLogin: () -> Unit) {
    val navigationState = rememberNavigationState(
        startRoute = Home,
        topLevelRoutes = topLevelRoutes.map { it.route }.toSet()
    )
    val internetAvailable: StateFlow<Boolean> = koinInject<ConnectivityObserver>().isConnected

    val navigator = remember { Navigator(navigationState) }
    val timetableViewModel: TimetableViewModel = koinViewModel()
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
            if (!internetAvailable.collectAsState().value) NoInternetIcon()
        }
    ) { paddingValues ->
        val entryProvider: (NavKey) -> NavEntry<NavKey> = entryProvider {
            entry<Attendance> {
                AttendanceCompose(attendanceViewModel, paddingValues)
            }
            entry<Iksica> {
                IksicaCompose(iksicaViewModel, paddingValues)
            }
            entry<Home> {
                HomeTabCompose(
                    homeViewModel = homeViewModel,
                    menzaViewModel = koinViewModel(),
                    innerPaddingValues = paddingValues,
                    navigate = navigator::navigate
                )
            }
            entry<Studomat> {
                StudomatCompose(studomatViewModel, paddingValues)
            }
            entry<TimeTable> {
                TimetableCompose(timetableViewModel, paddingValues)
            }
            entry<Settings> {
                val settingsViewModel: SettingsViewModel = koinViewModel()
                LaunchedEffect(settingsViewModel.routeToLogin.collectAsState().value) {
                    if (settingsViewModel.routeToLogin.value) {
                        routeToLogin()
                    }
                }

                SettingsCompose(
                    viewModel = settingsViewModel,
                    paddingValues = paddingValues,
                    goBack = navigator::goBack
                )
            }
        }
        NavDisplay(
            entries = navigationState.toEntries(entryProvider),
            onBack = { navigator.goBack() },
            transitionSpec = {
                ContentTransform(
                    targetContentEnter = fadeIn(tween(300)),
                    initialContentExit = fadeOut(tween(300))
                )
            },
            popTransitionSpec = {
                ContentTransform(
                    targetContentEnter = fadeIn(tween(300)),
                    initialContentExit = fadeOut(tween(300))
                )
            }
        )
    }
}
