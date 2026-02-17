package dev.etino.fcshared.screens.home.view

import android.util.Log
import androidx.compose.material3.SnackbarHostState
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
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char
import kotlinx.datetime.minus

@InternalCoroutinesApi
class HomeViewModel(
    private val noteRepository: NoteRepositoryInterface,
    private val weatherRepository: WeatherRepositoryInterface,
    private val timeTableRepository: TimeTableRepositoryInterface,
    private val userRepository: UserRepositoryInterface,
) : ViewModel() {

    // val internetAvailable: LiveData<Boolean> = InternetConnectionObserver.get()

    val snackbarHostState: SnackbarHostState = SnackbarHostState()
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

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d("HomeViewModel", "Caught $exception")
        viewModelScope.launch(Dispatchers.Main) {
            snackbarHostState.showSnackbar(
                "getApplication<Application>().applicationContext.getString( R.string.general_error)"

                //FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
                //FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
                //FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
            )
        }
    }

    init {
        getNotes()
        getForecast()
        loadUsersName()
    }

    private fun getForecast() {
        //if (internetAvailable.value == false) return
        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                weatherRepository.fetchWeatherDetails()?.let { _weatherDisplay.update { it } }
            } catch (e: Exception) {
                snackbarHostState.showSnackbar("getApplication<Application>().applicationContext.getString(R.string.weather_error)")
                //FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
                //FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
                //FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
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
        viewModelScope.launch(Dispatchers.IO + handler) {
            noteRepository.insert(note)
        }
    }

    fun delete(note: Note) {
        _notes.value = _notes.value?.minus(note)
        viewModelScope.launch(Dispatchers.IO + handler) {
            noteRepository.delete(note)
        }
    }

    fun fetchDailyTimetable() {
        //if (internetAvailable.value == false) return
        val date = LocalDate.now()
        val startDate: LocalDate =
            date.minus((date.dayOfWeek.ordinal - DayOfWeek.MONDAY.ordinal).toLong(), DateTimeUnit.DAY)
        val endDate: LocalDate =
            date.minus((date.dayOfWeek.ordinal - DayOfWeek.SATURDAY.ordinal).toLong(), DateTimeUnit.DAY)
        fetchDailyTimetable(startDate, endDate)
    }

    private fun getNotes() {
        viewModelScope.launch(Dispatchers.IO + handler) {
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

        viewModelScope.launch(Dispatchers.IO + handler) {
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
        viewModelScope.launch(Dispatchers.IO + handler) {
            val name = userRepository.getCurrentUser().fullName.split(" ").firstOrNull() ?: ""
            nameOfUser.update {
                name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase()
                    else it.toString()
                }
            }
        }
    }

    fun showSnackbar(message: String) {
        viewModelScope.launch(Dispatchers.Main + handler) {
            snackbarHostState.showSnackbar(message)
        }
    }
}
