package dev.etino.fcshared

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.etino.fcshared.compose.AppTheme
import dev.etino.fcshared.navigation.Appl
import dev.etino.fcshared.networking.networkModule
import dev.etino.fcshared.screens.attendance.di.attendanceModule
import dev.etino.fcshared.screens.login.di.loginModule
import dev.etino.fcshared.screens.timetable.di.timetableModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

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
            )
        }
        setContent {
            AppTheme() {
                Appl()
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    AppTheme() {
        Appl()
    }
}