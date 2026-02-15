package dev.etino.fcshared

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import dev.etino.fcshared.compose.AppTheme
import dev.etino.fcshared.screens.attendance.compose.AttendanceCompose
import dev.etino.fcshared.screens.attendance.view.AttendanceViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
@Composable
@Preview
fun App() {
    AppTheme() {
        Scaffold(){
            AttendanceCompose(koinViewModel<AttendanceViewModel>(), innerPaddingValues = it)
        }
    }
}