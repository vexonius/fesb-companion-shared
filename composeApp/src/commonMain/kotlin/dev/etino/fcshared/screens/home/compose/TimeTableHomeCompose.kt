package dev.etino.fcshared.screens.home.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.etino.fcshared.compose.AppTheme
import dev.etino.fcshared.screens.timetable.compose.schedule.until
import dev.etino.fcshared.testEvents
import dev.etino.fcshared.timetable.Event
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.getRest
import fesb_companion_shared.composeapp.generated.resources.smiley
import fesb_companion_shared.composeapp.generated.resources.time_range
import fesb_companion_shared.composeapp.generated.resources.todaysEvents
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration

@Composable
fun TodayTimetableCompose(events: List<Event>) {
    Column(
        modifier = Modifier.padding(12.dp, 12.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(Res.string.todaysEvents),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(12.dp, 0.dp),
        )

        if (events.isNotEmpty()) {
            events.forEach { event -> TimetableItem(event) }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.smiley),
                    contentDescription = "Smiley",
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(60.dp)
                        .aspectRatio(1f)
                )
                Text(
                    text = stringResource(Res.string.getRest),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(top = 15.dp, bottom = 10.dp),
                )
            }

        }
    }
}

@Composable
fun TimetableItem(event: Event) {

    val hourFormatter = LocalTime.Format {
        hour()
        char(':')
        minute()
    }

    val expanded = remember { mutableStateOf(false) }
    Column(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { expanded.value = !expanded.value }
            .padding(12.dp, 5.dp)
            .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val dividerColor = MaterialTheme.colorScheme.outline
            val dividerWidth = 1.dp
            val hours=  event.start.time.until(event.end.time, DateTimeUnit.MINUTE)/60f
            val time = hours * 6
            Canvas(modifier = Modifier.size(Dp(time * 5.dp.value) + 5.dp, 10.dp)) {
                val radius = 5.dp.toPx()
                val width = (radius * time - radius).coerceAtLeast(radius)
                val dividerFreq = width.div(hours)
                drawLine(
                    color = event.color,
                    start = Offset(radius, radius),
                    end = Offset(width, radius),
                    strokeWidth = radius * 2,
                    cap = StrokeCap.Round,
                )
                for (i in 1 until hours.toInt().plus(1)) {
                    drawLine(
                        color = dividerColor.copy(alpha = 0.5f),
                        start = Offset(dividerFreq * i, radius),
                        end = Offset(dividerFreq * i + dividerWidth.toPx(), radius),
                        strokeWidth = radius * 2,
                    )
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = event.eventType.value + " • " + event.classroom,
                fontSize = 12.sp,
            )
        }
        Row {
            if (!expanded.value) {
                Text(
                    text = event.start.time.format(hourFormatter),
                    fontSize = 16.sp,
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(
                text = event.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        if (expanded.value) {
            Row {
                Text(
                    text = stringResource(
                        Res.string.time_range,
                        event.start.time.format(hourFormatter),
                        event.end.time.format(hourFormatter)
                    ),
                    fontSize = 12.sp
                )
                Text(
                    text = event.professor,
                    fontSize = 12.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
fun TodayTimetablePreview() {
    AppTheme {
        Surface {
            TodayTimetableCompose(testEvents)
        }
    }
}