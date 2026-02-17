package dev.etino.fcshared

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.etino.fcshared.compose.AppTheme
import dev.etino.fcshared.navigation.Application
import dev.etino.fcshared.networking.networkModule
import dev.etino.fcshared.screens.attendance.di.attendanceModule
import dev.etino.fcshared.screens.home.di.homeModule
import dev.etino.fcshared.screens.login.di.loginModule
import dev.etino.fcshared.screens.timetable.di.timetableModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.java.KoinJavaComponent.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(this@MainActivity)
            androidLogger(level = Level.ERROR)
            modules(
                attendanceModule,
                loginModule,
                dbModule,
                networkModule,
                timetableModule,
                homeModule
            )
        }
        val datastore: DataStore<Preferences> by inject(DataStore::class.java)
        val loggedIn =  runBlocking{
            datastore.data.map { it[SPKey.LOGGED_IN.key] ?: false }.first()
        }

        setContent {
            AppTheme() {
                Application(loggedIn)
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    AppTheme() {
        Application(true)
    }
}