package dev.etino.fcshared.timetable.parser

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import dev.etino.fcshared.Utils.Parsable
import dev.etino.fcshared.timetable.models.Event
import dev.etino.fcshared.timetable.models.Recurring
import dev.etino.fcshared.timetable.models.TimetableType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

class TimetableParser: Parsable<String, List<Event>> {

    override fun parse(data: String): List<Event> {
        val doc: Document = Ksoup.parse(data)

        val elements = doc.select("div.event")

        val events = elements
            .map {
                val id = it.attr("data-id").toInt()
                val startdate = it.attr("data-startsdate")
                val starth = it.attr("data-startshour").toInt()
                val startmin = it.attr("data-startsmin").toInt()
                val enddate = it.attr("data-endsdate")
                val endh = it.attr("data-endshour").toInt()
                val endmin = it.attr("data-endsmin").toInt()
                val type = parseType(it.selectFirst("span.groupCategory")?.text()?.split(",")?.get(0) ?: "")
                val name = it.selectFirst("span.name.normal")?.text()
                    ?:it.selectFirst("div.popup > div.eventContent > div.header > div > span.title")?.text()
                    ?: ""
                val group = it.select("span.group.normal").first()?.text() ?: ""
                val studycode = it.selectFirst("span.studyCode")?.text() ?: ""
                val room = it.selectFirst("div.eventContent > div.eventInfo > span.resource")?.text() ?: ""
                val detailTime = it.selectFirst("div.detailItem.datetime")?.text() ?: ""
                val professor = it.selectFirst("div.detailItem.user")?.text() ?: ""
                val repetsType = parseRecurring(it.selectFirst("div.recurring > span.type > span"))
                val isItRecurring = !(repetsType == Recurring.ONCE || repetsType == Recurring.UNDEFINED)

                val repeatsUntil = it.selectFirst("span.repeat")?.text() ?: ""

                return@map Event(
                    id = id.toString(),
                    name = name,
                    shortName = makeAcronym(name),
                    professor = professor,
                    eventType = type,
                    groups = group,
                    classroom = room,
                    start = LocalDateTime(
                        LocalDate.parse(startdate),
                        LocalTime(starth, startmin)
                    ).toString(),
                    end = LocalDateTime(
                        LocalDate.parse(enddate),
                        LocalTime(endh, endmin)
                    ).toString(),
                    description = detailTime,
                    recurring = isItRecurring,
                    recurringType = repetsType,
                    recurringUntil = repeatsUntil,
                    studyCode = studycode
                )
        }

        return events
    }

    private fun parseType(value: String): TimetableType = TimetableType
        .entries.firstOrNull { it.value == value } ?: TimetableType.OTHER

    private fun parseRecurring(element: Element?): Recurring {
        return when {
            element == null -> Recurring.ONCE
            element.hasClass("weekly") -> Recurring.WEEKLY
            element.hasClass("everyTwoWeeks") -> Recurring.EVERY_TWO_WEEKS
            element.hasClass("monthly") -> Recurring.MONTHLY
            else -> Recurring.UNDEFINED
        }
    }

    private fun makeAcronym(name: String): String {
        val acronym = StringBuilder()
        if (name.isNotEmpty() && name.contains(" ")) {
            val nameSplit = name.split(" ").toTypedArray()
            for (str in nameSplit)
                acronym.append(str[0])
            return acronym.toString().uppercase()
        }

        return name
    }

}