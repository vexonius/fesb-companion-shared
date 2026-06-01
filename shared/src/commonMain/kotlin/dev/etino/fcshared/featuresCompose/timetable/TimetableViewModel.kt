package dev.etino.fcshared.featuresCompose.timetable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.minusDays
import com.kizitonwose.calendar.core.minusMonths
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusMonths
import dev.etino.fcshared.featuresKotlin.login.user.UserRepositoryInterface
import dev.etino.fcshared.featuresKotlin.timetable.Event
import dev.etino.fcshared.featuresKotlin.timetable.MonthData
import dev.etino.fcshared.featuresKotlin.timetable.TimeTableInfo
import dev.etino.fcshared.featuresKotlin.timetable.repository.interfaces.TimeTableRepositoryInterface
import dev.etino.fcshared.networking.ConnectivityObserver
import fesb_companion_shared.shared.generated.resources.Res
import fesb_companion_shared.shared.generated.resources.general_error
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    val internetAvailable: StateFlow<Boolean> = connectivityObserver.isConnected

    private val _currentEventShown = MutableStateFlow<Event?>(null)
    val currentEventShown: StateFlow<Event?> = _currentEventShown

    private var _events = MutableStateFlow<List<Event>>(emptyList())//timeTableRepository.events.first())
    var events: StateFlow<List<Event>> = _events

    private val _showSnackbar = MutableSharedFlow<StringResource>()
    val showSnackbar = _showSnackbar.asSharedFlow()

    fun showMessage(resId: StringResource) {
        viewModelScope.launch {
            _showSnackbar.emit(resId)
        }
    }

    private val _daysInPeriods = MutableStateFlow<Map<LocalDate, TimeTableInfo>>(mutableMapOf())
    val daysInPeriods: StateFlow<Map<LocalDate, TimeTableInfo>> = _daysInPeriods

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
            showMessage(Res.string.general_error)
            print(exception)
        }
    }

    init {
        fetchTimetableAgenda()
    }

    fun resetToCurrentWeek() {
        viewModelScope.launch(Dispatchers.Default + handler) {
            _events.value = timeTableRepository.getCachedEvents()
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

    private fun fetchTimetableAgenda(
        startDate: String = (LocalDate.now().year - 1).toString() + "-8-1",
        endDate: String = (LocalDate.now().year + 1).toString() + "-8-1"
    ) {
        if (!internetAvailable.value) return
        viewModelScope.launch(Dispatchers.Default + handler) {
            _daysInPeriods.value = timeTableRepository.fetchTimeTableCalendar(startDate, endDate)
        }
    }

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