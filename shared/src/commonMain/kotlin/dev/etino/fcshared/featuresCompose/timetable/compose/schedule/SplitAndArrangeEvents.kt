package dev.etino.fcshared.featuresCompose.timetable.compose.schedule

import com.kizitonwose.calendar.core.plusDays
import dev.etino.fcshared.featuresKotlin.timetable.Event
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalTime
import kotlinx.datetime.until


fun splitEvents(events: List<Event>): List<PositionedEvent> {
    return events.map { event ->
        val startDate = event.start.date
        val endDate = event.end.date
        if (startDate == endDate) {
            listOf(
                PositionedEvent(
                    event,
                    SplitType.None,
                    event.start.date,
                    event.start.time,
                    event.end.time
                )
            )
        } else {
            val days = startDate.until(endDate, DateTimeUnit.DAY).toInt()
            val splitEvents = mutableListOf<PositionedEvent>()
            for (i in 0..days) {
                val date = startDate.plusDays(i)
                splitEvents += PositionedEvent(
                    event,
                    splitType = if (date == startDate) SplitType.End else if (date == endDate) SplitType.Start else SplitType.Both,
                    date = date,
                    start = if (date == startDate) event.start.time else LocalTime(0, 0, 0),
                    end = if (date == endDate) event.end.time else LocalTime(23, 59, 59),
                )
            }
            splitEvents
        }
    }.flatten()
}

fun arrangeEvents(events: List<PositionedEvent>): List<PositionedEvent> {
    /**
     * Final list of events with their positions
     */
    val positionedEvents = mutableListOf<PositionedEvent>()

    /**
     * List of columns, each column is a list of events that are in that column
     */
    val columnsOfEvents: MutableList<MutableList<PositionedEvent>> = mutableListOf()

    fun moveElementsFromGroup() {
        columnsOfEvents.forEachIndexed { columnIndex, groupColumn ->
            groupColumn.forEach { event ->
                positionedEvents.add(event.copy(column = columnIndex, columnTotal = columnsOfEvents.size))
            }
        }
        columnsOfEvents.clear()
    }

    events.forEach { eventToAdd ->
        /**
         * Value is -1 if there is no free column, otherwise it's the index of the first free column*/
        var firstFreeColumn = -1

        /**
         * Number of free columns after the first free column*/
        var numberOfFreeColumns = 0

        //Goes trough all columns and checks if the event overlaps with any of the events in the column.
        //If it does, it checks how many columns are free after the first non-overlapping event.
        //So it ends up with the first free column and the number of free columns after it.

        for (i in 0 until columnsOfEvents.size) {
            if (columnsOfEvents[i].anyEventOverlapsWith(eventToAdd)) {
                if (firstFreeColumn < 0) continue else break
            }
            if (firstFreeColumn < 0) firstFreeColumn = i
            numberOfFreeColumns++
        }
        val noOverlap = numberOfFreeColumns == columnsOfEvents.size
        val overlapsWithAll = firstFreeColumn < 0
        when {
            noOverlap -> {
                moveElementsFromGroup()
                columnsOfEvents += mutableListOf(eventToAdd)
            }

            overlapsWithAll -> {
                columnsOfEvents += mutableListOf(eventToAdd)
                // Expand anything that spans into the previous column and doesn't overlap with this event
                for (columnIndex in 0 until columnsOfEvents.size - 1) {
                    val column = columnsOfEvents[columnIndex]
                    column.forEachIndexed { eIndex, eventInColumn ->
                        if (columnIndex + eventInColumn.columnSpan == columnsOfEvents.size - 1 && !eventInColumn.overlapsWith(
                                eventToAdd
                            )
                        ) {
                            column[eIndex] = eventInColumn.copy(columnSpan = +1)
                        }
                    }
                }
            }
            // At least one column free, add to first free column and expand to as many as possible
            else -> {
                columnsOfEvents[firstFreeColumn] += eventToAdd.copy(columnSpan = numberOfFreeColumns)
            }
        }
    }
    moveElementsFromGroup()
    return positionedEvents
}