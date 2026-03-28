package dev.etino.fcshared

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.etino.fcshared.application.Application
import dev.etino.fcshared.application.ApplicationWrapper
import dev.etino.fcshared.compose.AppTheme
import dev.etino.fcshared.features.attendance.di.attendanceModule
import dev.etino.fcshared.features.home.di.homeModule
import dev.etino.fcshared.features.iksica.di.iksicaModule
import dev.etino.fcshared.features.login.di.loginModule
import dev.etino.fcshared.features.menza.di.menzaModule
import dev.etino.fcshared.features.settings.di.settingsModule
import dev.etino.fcshared.features.studomat.di.studomatModule
import dev.etino.fcshared.features.timetable.di.timetableModule
import dev.etino.fcshared.networking.servicesModule
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
                androidKoinModuleDB,
                servicesModule,
                timetableModule,
                homeModule,
                settingsModule,
                menzaModule,
                iksicaModule,
                studomatModule,
            )
        }

        setContent {
            ApplicationWrapper()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    AppTheme {
        Application {}
    }
}