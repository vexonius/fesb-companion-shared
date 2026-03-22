package dev.etino.fcshared.features.home.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kizitonwose.calendar.core.now
import dev.etino.fcshared.home.models.Note
import dev.etino.fcshared.home.models.WeatherDisplay
import dev.etino.fcshared.home.repository.NoteRepositoryInterface
import dev.etino.fcshared.home.repository.WeatherRepositoryInterface
import dev.etino.fcshared.login.user.UserRepositoryInterface
import dev.etino.fcshared.timetable.Event
import dev.etino.fcshared.timetable.repository.interfaces.TimeTableRepositoryInterface
import dev.jordond.connectivity.Connectivity
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.general_error
import fesb_companion_shared.composeapp.generated.resources.weather_error
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import org.jetbrains.compose.resources.StringResource

@InternalCoroutinesApi
class HomeViewModel(
    private val noteRepository: NoteRepositoryInterface,
    private val weatherRepository: WeatherRepositoryInterface,
    private val timeTableRepository: TimeTableRepositoryInterface,
    private val userRepository: UserRepositoryInterface,
    private val connectivity: Connectivity
) : ViewModel() {

    val internetAvailable: StateFlow<Boolean> =
        connectivity.statusUpdates
            .map { it.isConnected }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = false
            )
    private val _weatherDisplay = MutableStateFlow<WeatherDisplay?>(null)
    private val _notes = MutableStateFlow<List<Note>?>(null)
    val nameOfUser = MutableStateFlow<String?>(null)
    val weatherDisplay: StateFlow<WeatherDisplay?> = _weatherDisplay
    val notes: StateFlow<List<Note>?> = _notes
    val events: StateFlow<List<Event>> =
        timeTableRepository.events.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )
    private val _showSnackbar = MutableStateFlow<StringResource?>(null)
    val showSnackbar: StateFlow<StringResource?> = _showSnackbar

    private val handler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch(Dispatchers.Main) {
            _showSnackbar.update { Res.string.general_error }
        }
    }

    init {
        getNotes()
        getForecast()
        loadUsersName()
    }

    private fun getForecast() {
        if (!internetAvailable.value) return
        viewModelScope.launch(Dispatchers.Default + handler) {
            try {
                weatherRepository.fetchWeatherDetails()?.let { _weatherDisplay.update { it } }
            } catch (e: Exception) {
                _showSnackbar.update { Res.string.weather_error }
            }
        }
    }

    fun insert(note: Note) {
        if (_notes.value?.any { it.id == note.id } == true)
            _notes.value?.map {
                if (it.id == note.id) {
                    it.checked = note.checked
                }
            }
        else {
            _notes.value = _notes.value?.plus(note)
        }
        viewModelScope.launch(Dispatchers.Default + handler) {
            noteRepository.insert(note)
        }
    }

    fun delete(note: Note) {
        _notes.value = _notes.value?.minus(note)
        viewModelScope.launch(Dispatchers.Default + handler) {
            noteRepository.delete(note)
        }
    }

    fun fetchDailyTimetable() {
        if (!internetAvailable.value) return
        val date = LocalDate.now()
        val startDate: LocalDate =
            date.minus((date.dayOfWeek.ordinal - DayOfWeek.MONDAY.ordinal).toLong(), DateTimeUnit.DAY)
        val endDate: LocalDate =
            date.minus((date.dayOfWeek.ordinal - DayOfWeek.SATURDAY.ordinal).toLong(), DateTimeUnit.DAY)
        fetchDailyTimetable(startDate, endDate)
    }

    private fun getNotes() {
        viewModelScope.launch(Dispatchers.Default + handler) {
            val notes = noteRepository.getNotes()
            _notes.update { notes }
        }
    }

    private fun fetchDailyTimetable(
        startDate: LocalDate,
        endDate: LocalDate
    ) {
        val dateFormatter = LocalDate.Format {
            monthNumber()
            char('-')
            day()
            char('-')
            year()
        }
        val startDateFormated = dateFormatter.format(startDate)
        val endDateFormated = dateFormatter.format(endDate)

        viewModelScope.launch(Dispatchers.Default + handler) {
            val username = userRepository.getCurrentUserName()
            timeTableRepository.fetchTimetable(username, startDateFormated, endDateFormated, true)
        }
    }

    fun launchStudentskiUgovoriApp() {
        /*val context = getApplication<Application>().applicationContext
        val appPackageName = "com.ugovori.studentskiugovori"
        val intent = context.packageManager.getLaunchIntentForPackage(appPackageName) ?: Intent(
            Intent.ACTION_VIEW,
            "https://play.google.com/store/apps/details?id=$appPackageName".toUri()
        )
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))*/
    }

    fun loadUsersName() {
        viewModelScope.launch(Dispatchers.Default + handler) {
            val name = userRepository.getCurrentUser().fullName.split(" ").firstOrNull() ?: ""
            nameOfUser.update {
                name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase()
                    else it.toString()
                }
            }
        }
    }
}
