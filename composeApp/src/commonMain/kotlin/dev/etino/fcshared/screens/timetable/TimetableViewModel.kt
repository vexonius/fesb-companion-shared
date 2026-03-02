package dev.etino.fcshared.screens.timetable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.minusDays
import com.kizitonwose.calendar.core.minusMonths
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusMonths
import dev.etino.fcshared.login.user.UserRepositoryInterface
import dev.etino.fcshared.timetable.Event
import dev.etino.fcshared.timetable.MonthData
import dev.etino.fcshared.timetable.repository.interfaces.TimeTableRepositoryInterface
import dev.jordond.connectivity.Connectivity
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.general_error
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.format.char
import org.jetbrains.compose.resources.StringResource

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TimetableViewModel(
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
    private val _currentEventShown = MutableStateFlow<Event?>(null)
    val currentEventShown: StateFlow<Event?> = _currentEventShown

    private var _events = MutableStateFlow<List<Event>>(emptyList())//timeTableRepository.events.first())
    var events: StateFlow<List<Event>> = _events

    private val _showSnackbar = MutableStateFlow<StringResource?>(null)
    val showSnackbar: StateFlow<StringResource?> = _showSnackbar

    /*private val _daysInPeriods = MutableStateFlow<Map<LocalDate, TimeTableInfo>>(mutableMapOf())
    val daysInPeriods: StateFlow<Map<LocalDate, TimeTableInfo>> = _daysInPeriods*/

    private val _mondayOfSelectedWeek: MutableStateFlow<LocalDate> = MutableStateFlow<LocalDate>(
        LocalDate.now().let { it.minusDays((it.dayOfWeek.ordinal - DayOfWeek.MONDAY.ordinal)) })
    val mondayOfSelectedWeek: StateFlow<LocalDate> = _mondayOfSelectedWeek

    private val _showWeekChooseMenu = MutableStateFlow(false)
    val shownWeekChooseMenu: StateFlow<Boolean> = _showWeekChooseMenu

    val monthData = MutableStateFlow(
        MonthData(
            currentMonth = YearMonth.now(),
            startMonth = YearMonth.now().minusMonths(100),
            endMonth = YearMonth.now().plusMonths(100),
            firstDayOfWeek = firstDayOfWeekFromLocale()
        )
    )

    private val handler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch(Dispatchers.Main) {
            _showSnackbar.update { Res.string.general_error }
        }
    }

    init {
        //fetchTimetableAgenda()
    }

    fun resetToCurrentWeek() {
        viewModelScope.launch(Dispatchers.Default + handler) {
            timeTableRepository.events.collect { _events.value = it }
        }
        _mondayOfSelectedWeek.value =
            LocalDate.now().let { it.minusDays(it.dayOfWeek.ordinal - DayOfWeek.MONDAY.ordinal) }
    }

    fun fetchUserTimetable() {
        if (!internetAvailable.value)
            return
        val today = LocalDate.now()
        val startDate: LocalDate = today.minusDays(today.dayOfWeek.ordinal - DayOfWeek.MONDAY.ordinal)
        val endDate: LocalDate = today.minusDays(today.dayOfWeek.ordinal - DayOfWeek.SATURDAY.ordinal)
        fetchUserTimetable(startDate, endDate, startDate, shouldCache = true)
    }

    fun fetchUserTimetable(date: LocalDate) {
        if (!internetAvailable.value) return
        val startDate: LocalDate = date.minusDays(date.dayOfWeek.ordinal - DayOfWeek.MONDAY.ordinal)
        val endDate: LocalDate = date.minusDays(date.dayOfWeek.ordinal - DayOfWeek.SATURDAY.ordinal)
        _mondayOfSelectedWeek.value = startDate
        fetchUserTimetable(startDate, endDate, startDate)
    }

    private fun fetchUserTimetable(
        startDate: LocalDate,
        endDate: LocalDate,
        shownWeekMonday: LocalDate,
        shouldCache: Boolean = false
    ) {
        if (!internetAvailable.value) return
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
            val items = timeTableRepository.fetchTimetable(username, startDateFormated, endDateFormated, shouldCache)
            _mondayOfSelectedWeek.value = shownWeekMonday
            _events.value = items
        }
    }

    /*private fun fetchTimetableAgenda(
        startDate: String = (LocalDate.now().year - 1).toString() + "-8-1",
        endDate: String = (LocalDate.now().year + 1).toString() + "-8-1"
    ) {
        if (internetAvailable.value == false) return
        viewModelScope.launch(Dispatchers.Default + handler) {
            _daysInPeriods.postValue(timeTableRepository.fetchTimeTableCalendar(startDate, endDate))
        }
    }*/

    fun showWeekChooseMenu(value: Boolean = true) {
        _showWeekChooseMenu.value = value
    }

    fun showEvent(event: Event) {
        _currentEventShown.value = event
    }

    fun hideEvent() {
        _currentEventShown.value = null
    }

}