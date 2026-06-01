package dev.etino.fcshared.featuresCompose.attendance.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.etino.fcshared.featuresCompose.attendance.ShownSemester
import dev.etino.fcshared.featuresKotlin.attendance.models.AttendanceEntry
import dev.etino.fcshared.featuresKotlin.attendance.repository.AttendanceRepositoryInterface
import dev.etino.fcshared.featuresKotlin.networking.NetworkServiceResult
import dev.etino.fcshared.networking.ConnectivityObserver
import fesb_companion_shared.shared.generated.resources.Res
import fesb_companion_shared.shared.generated.resources.general_error
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Clock

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class AttendanceViewModel(
    private val repository: AttendanceRepositoryInterface,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private var lastFetch = 0L
    private val has60SecondPassed: Boolean
        get() = Clock.System.now().toEpochMilliseconds() - lastFetch > 60000

    val internetAvailable: StateFlow<Boolean> = connectivityObserver.isConnected

    // Full list of attendance
    private val _attendanceListFull = MutableStateFlow<List<List<AttendanceEntry>>>(emptyList())
    val attendanceListFull: StateFlow<List<List<AttendanceEntry>>> = _attendanceListFull.asStateFlow()

    // Selected semester
    private val _shownSemester = MutableStateFlow<ShownSemester?>(null)
    val shownSemester: StateFlow<ShownSemester?> = _shownSemester.asStateFlow()

    // Filtered flows
    private val attendanceFirstSem: Flow<List<List<AttendanceEntry>>> =
        _attendanceListFull.map { list -> list.filter { it.firstOrNull()?.semester == 1 } }

    private val attendanceSecondSem: Flow<List<List<AttendanceEntry>>> =
        _attendanceListFull.map { list -> list.filter { it.firstOrNull()?.semester == 2 } }

    // Combined filtered attendance based on shown semester
    val attendance: Flow<List<List<AttendanceEntry>>> = _shownSemester.flatMapLatest { semester ->
        when (semester) {
            ShownSemester.FIRST -> attendanceFirstSem
            ShownSemester.SECOND -> attendanceSecondSem
            null -> _attendanceListFull
        }
    }

    private val _showSnackbar = MutableSharedFlow<StringResource>()
    val showSnackbar = _showSnackbar.asSharedFlow()

    fun showMessage(resId: StringResource) {
        viewModelScope.launch {
            _showSnackbar.emit(resId)
        }
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch(Dispatchers.Main) {
            showMessage(Res.string.general_error)
        }
    }

    init {
        loadFromDb()
        fetchAttendance()
    }

    fun fetchAttendance() {
        if (!has60SecondPassed) return
        if (!internetAvailable.value) return
        viewModelScope.launch(context = Dispatchers.Default + handler) {
            lastFetch = Clock.System.now().toEpochMilliseconds()
            when (val attendance = repository.fetchAttendance()) {
                is NetworkServiceResult.AttendanceParseResult.Success -> {
                    val data = attendance.data
                    _attendanceListFull.value = data
                }

                is NetworkServiceResult.AttendanceParseResult.Failure -> {
                    showMessage(Res.string.general_error)
                }
            }
        }
    }

    private fun loadFromDb() {
        viewModelScope.launch(context = Dispatchers.IO + handler) {
            _attendanceListFull.value = repository.readAttendance()
        }
    }

    fun showSemester(semester: ShownSemester) {
        _shownSemester.value = if (_shownSemester.value == semester) null else semester
    }

}

