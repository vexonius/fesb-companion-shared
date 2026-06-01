package dev.etino.fcshared.featuresCompose.timetable.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.etino.fcshared.featuresKotlin.timetable.Event
import fesb_companion_shared.shared.generated.resources.Res
import fesb_companion_shared.shared.generated.resources.classroom
import fesb_companion_shared.shared.generated.resources.group
import fesb_companion_shared.shared.generated.resources.professor
import fesb_companion_shared.shared.generated.resources.recurring
import fesb_companion_shared.shared.generated.resources.time
import fesb_companion_shared.shared.generated.resources.time_range
import org.jetbrains.compose.resources.stringResource

@Composable
fun EventBottomSheet(event: Event) {
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(20.dp, 5.dp, 15.dp, 20.dp)
            .fillMaxSize()
    ) {
        Text(
            text = event.name,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(0.dp, 15.dp, 15.dp, 8.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            val radius = 6.dp
            Canvas(modifier = Modifier.size(radius * 2)) { drawCircle(color = event.color, radius = radius.toPx()) }
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = event.eventType.value, style = MaterialTheme.typography.titleSmall)
        }
        val modifier = Modifier.padding(bottom = 16.dp, end = 8.dp)
        Column(Modifier.fillMaxWidth()) {
            Row {
                RowItem(
                    title = stringResource(Res.string.professor),
                    text = event.professor,
                    modifier = modifier.weight(9.5f),
                )
                RowItem(
                    title = stringResource(Res.string.group),
                    text = event.groups.split(",").firstOrNull() ?: "",
                    modifier = modifier.weight(9.5f),
                )
            }
            Row {
                RowItem(
                    title = stringResource(Res.string.time),
                    text = stringResource(
                        Res.string.time_range,
                        event.start.time,
                        event.end.time
                    ),
                    modifier = modifier.weight(9.5f),
                )
                RowItem(
                    title = stringResource(Res.string.classroom),
                    text = event.classroom,
                    modifier = modifier.weight(9.5f),
                )
            }
            Row {
                RowItem(
                    title = stringResource(Res.string.recurring),
                    text = event.recurringUntil,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun RowItem(
    title: String,
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 2.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(text = text, style = MaterialTheme.typography.labelSmall)
    }

}
/*

@Preview
@Composable
fun EventBottomSheetPreview() {
    AppTheme {
        Surface {
            EventBottomSheet(
                event = testEvents.first()
            )
        }
    }
}*/
