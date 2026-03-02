package dev.etino.fcshared.screens.menza.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.etino.fcshared.menza.MenzaResult
import dev.etino.fcshared.menza.models.Menza
import dev.etino.fcshared.menza.models.MenzaLocation
import dev.etino.fcshared.menza.models.menzaLocations
import dev.etino.fcshared.menza.repository.CamerasRepositoryInterface
import dev.etino.fcshared.menza.repository.MenzaRepositoryInterface
import dev.etino.fcshared.now
import dev.jordond.connectivity.Connectivity
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


@InternalCoroutinesApi
class MenzaViewModel(
    private val menzaRepository: MenzaRepositoryInterface,
    private val camerasRepository: CamerasRepositoryInterface,
    private val connectivity: Connectivity
) : ViewModel() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        //Log.d("MenzaViewModel", "CoroutineExceptionHandler got $exception")
        println(exception)
    }

    private val _images = MutableStateFlow<Pair<MenzaLocation, Url?>?>(null)
    val images: StateFlow<Pair<MenzaLocation, Url?>?> = _images
    val internetAvailable: StateFlow<Boolean> =
        connectivity.statusUpdates
            .map { it.isConnected }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = false
            )

    private val _menza = MutableStateFlow<List<Pair<MenzaLocation, Menza?>>?>(null)
    val menza: StateFlow<List<Pair<MenzaLocation, Menza?>>?> = _menza

    val menzaOpened: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var updateUrlsJob: Job? = null

    init {
        fetchMenza()
    }

    private fun fetchMenza() {
        if (!internetAvailable.value) return
        viewModelScope.launch(Dispatchers.Default + coroutineExceptionHandler) {
            val test = menzaLocations.map {
                it to when (val menza = menzaRepository.fetchMenzaDetails(it.meniName, false)) {
                    is MenzaResult.Success -> {
                        menza.data
                    }

                    is MenzaResult.Failure -> {
                        println("Greška prilikom dohvaćanja menze")
                        null
                    }
                }
            }
            _menza.update {
                test
            }
        }
    }


    private fun getImageUrlApproximately(location: MenzaLocation) {
        val minuteAgo = LocalDateTime.now()
            .toInstant(TimeZone.currentSystemDefault())
            .minus(2, DateTimeUnit.MINUTE)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val filename = LocalDateTime.Format {
            year()
            char('-')
            monthNumber()
            char('-')
            day()
            char('_')
            hour()
            char('i')
            minute()
            chars("i00.jpg")
        }.format(minuteAgo)
        _images.value =
            location to URLBuilder(
                protocol = URLProtocol.HTTPS,
                host = "camerasfiles.dbtouch.com",
                pathSegments = listOf("images", location.cameraName, filename)
            ).build()

    }

    private fun getImageUrl(location: MenzaLocation) {
        viewModelScope.launch(Dispatchers.Default + coroutineExceptionHandler) {
            _images.update { location to camerasRepository.getImages(location.cameraName) }
        }
    }

    fun updateMenzaUrl(location: MenzaLocation) {
        _images.value = null
        updateUrlsJob?.cancel()

        val interval = 20

        updateUrlsJob = viewModelScope.launch {
            getImageUrlApproximately(location)
            getImageUrl(location)
            while (isActive) {
                if (LocalTime.now().second.mod(interval) == 4) {
                    getImageUrl(location)
                }
                delay(1000L)
            }
        }
    }

    private fun cancelUpdateUrlJob() {
        updateUrlsJob?.cancel()
    }

    fun openMenza() {
        menzaOpened.update { true }
        fetchMenza()
    }

    fun closeMenza() {
        cancelUpdateUrlJob()
        menzaOpened.update { false }
    }

}
