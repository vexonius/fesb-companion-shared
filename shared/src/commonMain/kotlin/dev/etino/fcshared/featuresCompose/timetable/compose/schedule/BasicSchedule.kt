package dev.etino.fcshared.featuresCompose.timetable.compose.schedule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.now
import dev.etino.fcshared.featuresKotlin.timetable.Event
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.daysUntil
import kotlin.math.roundToInt

@Composable
fun BasicSchedule(
    events: List<Event>,
    modifier: Modifier = Modifier,
    eventContent: @Composable (positionedEvent: PositionedEvent) -> Unit = {
        BasicEvent(positionedEvent = it, onClick = { })
    },
    minDate: LocalDate = events.minByOrNull(Event::start)?.start?.date ?: LocalDate.now(),
    maxDate: LocalDate = events.maxByOrNull(Event::end)?.end?.date ?: LocalDate.now(),
    minDayTime: LocalTime = LocalTime(0, 0),
    maxDayTime: LocalTime = LocalTime(23, 59, 59),
    dayWidth: Dp,
    hourHeight: Dp,
    eventsGlowing: Boolean = false
) {
    val numberOfDaysToShow = minDate.daysUntil(maxDate) + 1
    val numberOfMinutesToShow = (maxDayTime.toSecondOfDay() - minDayTime.toSecondOfDay()) / 60 + 1

    val numberOfHours = numberOfMinutesToShow / 60
    val positionedEvents = remember(events) {
        arrangeEvents(
            splitEvents(
                events.sortedBy(Event::start)
            )
        ).filter { it.end > minDayTime && it.start < maxDayTime }
    }
    val dividerColor = MaterialTheme.colorScheme.outline

    Box {
        Layout(content = {
            positionedEvents.forEach { positionedEvent ->
                Box(
                    modifier = Modifier
                        .eventData(positionedEvent)
                        .padding(horizontal = 2.dp)
                ) {
                    eventContent(positionedEvent)
                }
            }
        }, modifier = modifier.drawBehind {
            drawScheduleBackground(
                minDayTime = minDayTime,
                numberOfDaysToShow = numberOfDaysToShow,
                numberOfHours = numberOfHours,
                hourHeight = hourHeight,
                dayWidth = dayWidth,
                dividerColor = dividerColor,
            )
        }) { measureables, _ ->
            val dayWidthPx = dayWidth.toPx()
            val hourHeightPx = hourHeight.toPx()
            val height = (hourHeightPx * numberOfMinutesToShow / 60f).roundToInt()
            val width = dayWidthPx.roundToInt() * numberOfDaysToShow

            layout(width, height) {
                measureables.forEach { measurable ->
                    val splitEvent = measurable.parentData as PositionedEvent
                    val apparentStartOfEvent = minOf(splitEvent.end, maxDayTime)
                    val eventDurationMinutes =
                        (apparentStartOfEvent.toSecondOfDay() - splitEvent.start.toSecondOfDay()) / 60

                    val eventOffsetDays = minDate.daysUntil(splitEvent.date)
                    val eventOffsetMinutes =
                        ((splitEvent.start.toSecondOfDay() - minDayTime.toSecondOfDay()) / 60).takeIf { it > 0 } ?: 0


                    val eventColumnStart = splitEvent.column / splitEvent.columnTotal.toFloat()
                    val eventColumnSpanPercent = splitEvent.columnSpan / splitEvent.columnTotal.toFloat()

                    val eventHeight = (hourHeightPx * eventDurationMinutes / 60f).roundToInt()
                    val eventWidth = (dayWidthPx * eventColumnSpanPercent).roundToInt()

                    val eventYCoordinate = (hourHeightPx * eventOffsetMinutes / 60f).roundToInt()
                    val eventXCoordinate = (dayWidthPx * (eventOffsetDays + eventColumnStart)).roundToInt()
                    measurable.measure(
                        Constraints(
                            minWidth = eventWidth,
                            maxWidth = eventWidth,
                            minHeight = eventHeight,
                            maxHeight = eventHeight
                        )
                    ).place(eventXCoordinate, eventYCoordinate)

                }
            }
        }
    }
}