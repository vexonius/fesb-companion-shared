package dev.etino.fcshared.screens.attendance.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.etino.fcshared.attendance.models.AttendanceEntry
import dev.etino.fcshared.attendance.repository.AttendanceRepositoryInterface
import dev.etino.fcshared.networking.NetworkServiceResult
import dev.etino.fcshared.screens.attendance.ShownSemester
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.general_error
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class AttendanceViewModel(
    private val repository: AttendanceRepositoryInterface
) : ViewModel() {

    private var lastFetch = 0L
    private val has60SecondPassed: Boolean
        get() = System.currentTimeMillis() - lastFetch > 60000

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

    // --- Functions to update state ---
    fun setAttendanceList(list: List<List<AttendanceEntry>>) {
        _attendanceListFull.value = list
    }

    fun setShownSemester(semester: ShownSemester?) {
        _shownSemester.value = semester
    }

    private val _showSnackbar = MutableStateFlow<StringResource?>(null)
    val showSnackbar: StateFlow<StringResource?> = _showSnackbar


    private val handler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch(Dispatchers.Main) {
            _showSnackbar.update { Res.string.general_error }
        }
    }

    init {
        fetchAttendance()
    }

    fun fetchAttendance() {
        if (!has60SecondPassed) return
        viewModelScope.launch(context = Dispatchers.IO + handler) {
            lastFetch = System.currentTimeMillis()
            when (val attendance = repository.fetchAttendance()) {
                is NetworkServiceResult.AttendanceParseResult.Success -> {
                    val data = attendance.data
                    _attendanceListFull.value = data
                }

                is NetworkServiceResult.AttendanceParseResult.Failure -> {
                    _showSnackbar.update { Res.string.general_error }
                }
            }
        }
    }


    fun showSemester(semester: ShownSemester) {
        _shownSemester.value = if (_shownSemester.value == semester) null else semester
    }

}

