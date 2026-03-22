package dev.etino.fcshared.features.attendance.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.etino.fcshared.compose.contentColors
import dev.etino.fcshared.features.attendance.ShownSemester
import dev.etino.fcshared.features.attendance.view.AttendanceViewModel
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.first_semester
import fesb_companion_shared.composeapp.generated.resources.second_semester
import fesb_companion_shared.composeapp.generated.resources.tab_attendance
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
@OptIn(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun AttendanceCompose(attendanceViewModel: AttendanceViewModel, innerPaddingValues: PaddingValues) {

    val items by attendanceViewModel.attendanceListFull.collectAsState(initial = emptyList())

    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsState()
    val snackbarHostState = SnackbarHostState()

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.RESUMED -> {
                attendanceViewModel.fetchAttendance()
            }

            else -> {}
        }
    }

    val message = attendanceViewModel.showSnackbar.collectAsState().value?.let { stringResource(it) }
    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(message)
        }
    }

    Box(Modifier.padding(innerPaddingValues)){
        if (items.isNotEmpty()) {
            CreateAttendanceListView(attendanceViewModel, snackbarHostState)
        } else {
            EmptyView()
        }
    }
}

@Composable
fun EmptyView() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.contentColors.tertiary
        )
    }
}

@OptIn(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun CreateAttendanceListView(attendanceViewModel: AttendanceViewModel, snackbarHostState: SnackbarHostState) {
    val list by attendanceViewModel.attendance.collectAsState(initial = emptyList())
    val shownSemester by attendanceViewModel.shownSemester.collectAsState()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(Res.string.tab_attendance),
                modifier = Modifier.padding(32.dp, 40.dp, 0.dp, 8.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.contentColors.primary
            )
            Row(
                Modifier.padding(horizontal = 32.dp)
            ) {
                FilterButton(
                    selected = shownSemester == ShownSemester.FIRST,
                    text = stringResource(Res.string.first_semester),
                    onClick = { attendanceViewModel.showSemester(ShownSemester.FIRST) })
                FilterButton(
                    selected = shownSemester == ShownSemester.SECOND,
                    text = stringResource(Res.string.second_semester),
                    onClick = { attendanceViewModel.showSemester(ShownSemester.SECOND) })
            }

            list.forEach { item ->
                AttendanceItem(item)
            }
        }
    }
}

@Composable
fun FilterButton(
    selected: Boolean, text: String, onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .background(color = if (selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer),
    ) {
        Text(
            text = text,
            color = MaterialTheme.contentColors.primary,
            modifier = Modifier.padding(12.dp, 6.dp),
            fontSize = 14.sp
        )
    }
    Spacer(modifier = Modifier.padding(8.dp))
}

@Preview
@Composable
fun FilterButtonPreview() {
    MaterialTheme {
        FilterButton(selected = true, text = "First Semester", onClick = {})
    }
}