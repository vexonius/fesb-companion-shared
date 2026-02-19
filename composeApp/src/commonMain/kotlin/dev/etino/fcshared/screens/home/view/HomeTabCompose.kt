package dev.etino.fcshared.screens.home.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation3.runtime.NavKey
import dev.etino.fcshared.compose.AppTheme
import dev.etino.fcshared.home.models.Note
import dev.etino.fcshared.home.models.WeatherDisplay
import dev.etino.fcshared.navigation.Settings
import dev.etino.fcshared.now
import dev.etino.fcshared.screens.home.compose.CardsCompose
import dev.etino.fcshared.screens.home.compose.NotesCompose
import dev.etino.fcshared.screens.home.compose.TodayTimetableCompose
import dev.etino.fcshared.screens.home.utils.getWeatherText
import dev.etino.fcshared.screens.menza.view.MenzaCompose
import dev.etino.fcshared.screens.menza.view.MenzaViewModel
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.hi_user
import fesb_companion_shared.composeapp.generated.resources.settings_icon
import fesb_companion_shared.composeapp.generated.resources.weather_info
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

val sidePadding = 24.dp

@OptIn(ExperimentalMaterial3Api::class, InternalCoroutinesApi::class)
@Composable
fun HomeTabCompose(
    homeViewModel: HomeViewModel,
    menzaViewModel: MenzaViewModel,
    innerPaddingValues: PaddingValues,
    navigate: (NavKey) -> Unit,
) {

    val weather = homeViewModel.weatherDisplay
    val notes = homeViewModel.notes
    val events = homeViewModel.events
    val insertNote: (note: Note) -> Unit = homeViewModel::insert
    val deleteNote: (note: Note) -> Unit = homeViewModel::delete

    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.RESUMED -> {
                homeViewModel.fetchDailyTimetable()
            }

            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = homeViewModel.snackbarHostState) },
        contentWindowInsets = WindowInsets(0.dp),
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxHeight()) {
            if (menzaViewModel.menzaOpened.collectAsState().value) {
                MenzaCompose(menzaViewModel, innerPaddingValues)
                return@Scaffold
            }
            LazyColumn(
                Modifier
                    .padding(innerPaddingValues)
                    .padding(paddingValues)
            ) {
                item {
                    Row(
                        Modifier
                            .height(54.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.settings_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(top = 10.dp, end = 10.dp)
                                .size(32.dp)
                                .clip(CircleShape)
                                .clickable {
                                    navigate(Settings)
                                }
                        )
                    }
                }
                item {
                    WeatherCompose(
                        weather.collectAsState().value,
                        homeViewModel.nameOfUser.collectAsState().value ?: ""
                    )
                }
                item {
                    NotesCompose(
                        notes = notes.collectAsState().value ?: emptyList(),
                        insertNote,
                        deleteNote
                    )
                }
                item {
                    TodayTimetableCompose(
                        events.collectAsState().value?.filter { event -> event.start.date == LocalDate.now() }
                            ?: emptyList()
                    )
                }
                item { CardsCompose({ menzaViewModel.openMenza() }, homeViewModel) }
            }

        }
    }
}

@Composable
fun WeatherCompose(
    weather: WeatherDisplay?,
    nameOfUser: String
) {
    Column(
        modifier = Modifier.padding(32.dp, 0.dp, 0.dp, 0.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(Res.string.hi_user, nameOfUser),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold
        )
        if (weather != null) {
            Text(
                text = stringResource(
                    Res.string.weather_info,
                    weather.location,
                    stringResource(getWeatherText(weather.summary.lowercase())),
                    weather.temperature
                ),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Preview
@Composable
fun WeatherPreview() {
    AppTheme {
        Surface {
            WeatherCompose(
                weather = WeatherDisplay(
                    location = "Split",
                    temperature = 20.0,
                    summary = "rain"
                ),
                nameOfUser = "Marko"
            )
        }
    }
}