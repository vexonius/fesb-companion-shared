package dev.etino.fcshared.screens.studomat.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.etino.fcshared.networking.CustomCookieStorage
import dev.etino.fcshared.studomat.data.sortedByNameAndSemester
import dev.etino.fcshared.studomat.models.Student
import dev.etino.fcshared.studomat.models.StudomatYear
import dev.etino.fcshared.studomat.models.StudomatYearInfo
import dev.etino.fcshared.studomat.repository.StudomatRepository
import dev.etino.fcshared.studomat.repository.models.StudomatRepositoryResult
import fesb_companion_shared.composeapp.generated.resources.*
import com.multiplatform.webview.cookie.Cookie
import dev.jordond.connectivity.Connectivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource


class StudomatViewModel(
    private val repository: StudomatRepository,
    private val cookieStorage: CustomCookieStorage,
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

    val studomatCookie: MutableStateFlow<Cookie?> = MutableStateFlow(null)

    /**
     * StateFlow for refreshing state used for PullToRefresh
     */
    val isRefreshing = MutableStateFlow(false)
    val studomatData = MutableStateFlow<List<StudomatYear>>(emptyList())

    private var student = MutableStateFlow(Student())
    private var yearNames = MutableStateFlow<List<StudomatYearInfo>>(emptyList())

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _showSnackbar.update { Res.string.studomat_error_general }
        isRefreshing.update { false }
    }


    private val _showSnackbar = MutableStateFlow<StringResource?>(null)
    val showSnackbar: StateFlow<StringResource?> = _showSnackbar

    init {
        viewModelScope.launch(Dispatchers.Default + coroutineExceptionHandler) {
            studomatData.update { repository.readData() }
        }
        getStudomatData(getSubjects = false)
    }

    /**
     * Fetches student info and year names and the links for year pages from studomat
     */
    fun getStudomatData(pulldownTriggered: Boolean = false, getSubjects: Boolean = true) {
        if (!internetAvailable.value) return
        viewModelScope.launch(Dispatchers.Default + coroutineExceptionHandler) {
            if (pulldownTriggered) isRefreshing.update { true }
            when (val result = repository.getStudomatDataAndYears()) {
                is StudomatRepositoryResult.StudentAndYearsResult.Success -> {
                    student.update { result.student }
                    if (getSubjects) fetchAllYears(result.data, pulldownTriggered)
                    fetchStudomatCookie()
                }

                is StudomatRepositoryResult.StudentAndYearsResult.Failure -> {
                    _showSnackbar.update { Res.string.studomar_error }
                }
            }
        }
    }

    /**
     * Fetches subjects from each year from studomat
     */
    private fun fetchAllYears(
        freshYears: List<StudomatYearInfo>,
        pulldownTriggered: Boolean = false
    ) {
        if (!internetAvailable.value) return
        viewModelScope.launch(Dispatchers.Default + coroutineExceptionHandler) {
            if (pulldownTriggered) isRefreshing.update { true }
            val allYearsTemp = mutableListOf<StudomatYear>()
            freshYears.map { year ->
                async {
                    when (val result = repository.getYear(year)) {
                        is StudomatRepositoryResult.ChosenYearResult.Success -> {
                            val populatedYear = StudomatYear(
                                result.data.first,
                                result.data.second.sortedByNameAndSemester()
                            )
                            allYearsTemp.add(populatedYear)
                            launch { repository.insert(populatedYear) }
                        }

                        is StudomatRepositoryResult.ChosenYearResult.Failure -> {
                            _showSnackbar.update { Res.string.studomar_error }
                        }
                    }
                }
            }.awaitAll()
            val allYearsSorted = allYearsTemp.sortedByDescending { it.yearInfo.academicYear }
            val yearsInfo = allYearsSorted.map { it.yearInfo }
            studomatData.update { allYearsSorted }
            yearNames.update { yearsInfo }
            if (pulldownTriggered) isRefreshing.update { false }
        }
    }

    fun fetchStudomatCookie() {
        viewModelScope.launch(Dispatchers.Default + coroutineExceptionHandler) {
            cookieStorage.getISVUCookieForWebView()?.let {
                studomatCookie.value = Cookie(
                    name = it.name,
                    value = it.value,
                    domain = it.domain,
                    path = it.path,
                    expiresDate = null,
                    isSessionOnly = true,
                    isSecure = it.secure,
                    isHttpOnly = it.httpOnly,
                    maxAge = null,
                )
            }
        }
    }
}