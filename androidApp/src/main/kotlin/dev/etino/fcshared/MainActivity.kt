package dev.etino.fcshared

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.etino.fcshared.application.Application
import dev.etino.fcshared.application.ApplicationWrapper
import dev.etino.fcshared.compose.AppTheme
import dev.etino.fcshared.featuresCompose.attendance.di.attendanceModule
import dev.etino.fcshared.featuresCompose.home.di.homeModule
import dev.etino.fcshared.featuresCompose.iksica.di.iksicaModule
import dev.etino.fcshared.featuresCompose.login.di.loginModule
import dev.etino.fcshared.featuresCompose.menza.di.menzaModule
import dev.etino.fcshared.featuresCompose.settings.di.settingsModule
import dev.etino.fcshared.featuresCompose.studomat.di.studomatModule
import dev.etino.fcshared.featuresCompose.timetable.di.timetableModule
import dev.etino.fcshared.featuresKotlin.networking.servicesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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