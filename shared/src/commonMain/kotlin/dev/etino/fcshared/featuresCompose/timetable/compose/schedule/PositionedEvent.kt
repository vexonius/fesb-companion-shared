package dev.etino.fcshared.featuresCompose.timetable.compose.schedule

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density
import dev.etino.fcshared.featuresKotlin.timetable.Event
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.jvm.JvmInline


@JvmInline
value class SplitType private constructor(val value: Int) {
    companion object {
        val None = SplitType(0)
        val Start = SplitType(1)
        val End = SplitType(2)
        val Both = SplitType(3)
    }
}

data class PositionedEvent(
    val event: Event,
    val splitType: SplitType,
    val date: LocalDate,
    val start: LocalTime,
    val end: LocalTime,
    val column: Int = 0,
    val columnSpan: Int = 1,
    val columnTotal: Int = 1,
){
    fun overlapsWith(other: PositionedEvent): Boolean {
        return date == other.date && start < other.end && end > other.start
    }
}


class EventDataModifier(val positionedEvent: PositionedEvent) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = positionedEvent
}

fun Modifier.eventData(positionedEvent: PositionedEvent) = this.then(EventDataModifier(positionedEvent))


fun List<PositionedEvent>.anyEventOverlapsWith(event: PositionedEvent): Boolean {
    return any { it.overlapsWith(event) }
}