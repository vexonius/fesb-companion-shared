package dev.etino.fcshared.networking

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.etino.fcshared.SPKey
import dev.jordond.connectivity.Connectivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ConnectivityObserver(
    connectivity: Connectivity,
    dataStore: DataStore<Preferences>
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val isTestMode = dataStore.data.map { prefs -> prefs[SPKey.TEST_MODE.key]?.takeIf { it }  }


    private val _isConnected = connectivity.statusUpdates.map { it.isConnected }

    val isConnected = combine(_isConnected, isTestMode) { real, testMode ->
        testMode?.let { !it } ?: real
    }.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

}