package dev.etino.fcshared.features.attendance.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.etino.fcshared.attendance.models.AttendanceEntry
import dev.etino.fcshared.compose.accentGreen
import dev.etino.fcshared.compose.accentRed
import dev.etino.fcshared.compose.contentColors
import dev.etino.fcshared.compose.theme_dark_surface
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.attendance_stats_format
import org.jetbrains.compose.resources.stringResource


@Composable
fun AttendanceItem(attendanceItems: List<AttendanceEntry>) {
    Column(
        modifier = Modifier
            .padding(24.dp, 8.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(24.dp)
    ) {
        Text(
            text = attendanceItems.firstOrNull()?.subject ?: "",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.contentColors.primary
        )
        attendanceItems.forEach { attendanceItem ->
            Column {
                Text(
                    attendanceItem.type,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = MaterialTheme.contentColors.primary
                )
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    AttendanceProgressBar(
                        total = attendanceItem.total,
                        attended = attendanceItem.attended,
                        absent = attendanceItem.absent
                    )
                    Text(
                        text = stringResource(
                            Res.string.attendance_stats_format,
                            attendanceItem.attended,
                            attendanceItem.total,
                            attendanceItem.required
                        ),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = MaterialTheme.contentColors.secondary
                    )
                }
            }
        }
    }
}


@Composable
fun AttendanceProgressBar(
    total: Int,
    attended: Int,
    absent: Int,
    radius: Dp = 10.dp
) {
    val green = accentGreen
    val off = theme_dark_surface
    val red = accentRed
    Row {
        Canvas(
            Modifier
                .fillMaxWidth()
                .height(radius)
        ) {
            for (i in 0 until total) {
                drawCircle(
                    color = if (i < attended) green else if (i >= total - absent) red else off,
                    radius = radius.toPx(),
                    center = Offset(i * size.width / total + radius.toPx(), 0f),
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewAttendanceItem() {
    val attendanceItems = listOf(
        AttendanceEntry(
        ).apply {
            subject = "Class 1"
            type = "Type 1"
            total = 10
            attended = 5
            absent = 2
            required = 8
        },
        AttendanceEntry(
        ).apply {
            subject = "Class 1"
            type = "Type 2"
            total = 10
            attended = 5
            absent = 2
            required = 8
        }
    )
    AttendanceItem(attendanceItems)
}