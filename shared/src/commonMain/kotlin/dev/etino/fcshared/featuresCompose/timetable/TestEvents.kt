package dev.etino.fcshared.featuresCompose.timetable

import dev.etino.fcshared.compose.accentBlue
import dev.etino.fcshared.compose.accentRed
import dev.etino.fcshared.featuresKotlin.timetable.Event
import dev.etino.fcshared.featuresKotlin.timetable.TimetableType
import kotlinx.datetime.LocalDateTime

val testEvents = listOf(
    Event(
        id = "532059",
        name = "Kriptografija i mrežna sigurnost",
        shortName = "KIMS",
        color = accentRed,
        colorId = 2131099687,
        professor = "Čagalj Mario",
        eventType = TimetableType.PREDAVANJE,
        groups = "",
        classroom = "C501",
        start = LocalDateTime.parse("2024-04-29T10:15"),
        end = LocalDateTime.parse("2024-04-29T12:00"),
        description = "C501"
    ),
    Event(
        id = "534198",
        name = "Metode optimizacije",
        shortName = "MO",
        color = accentRed,
        colorId = 2131100480,
        professor = "Bašić Martina",
        eventType = TimetableType.LABORATORIJSKA_VJEZBA,
        groups = "Grupa 1,",
        classroom = "B420",
        start = LocalDateTime.parse("2024-04-29T18:30"),
        end = LocalDateTime.parse("2024-04-29T20:00"),
        description = "B420"
    ),
    Event(
        id = "532144",
        name = "Podržano strojno učenje",
        shortName = "PSU",
        color = accentBlue,
        colorId = 2131099687,
        professor = "Vasilj Josip",
        eventType = TimetableType.PREDAVANJE,
        groups = "",
        classroom = "A243",
        start = LocalDateTime.parse("2024-04-30T08:15"),
        end = LocalDateTime.parse("2024-04-30T10:00"),
        description = "A243"
    ),
    Event(
        id = "532084",
        name = "Metode optimizacije",
        shortName = "MO",
        color = accentBlue,
        colorId = 2131099687,
        professor = "Marasović Jadranka",
        eventType = TimetableType.PREDAVANJE,
        groups = "",
        classroom = "C502",
        start = LocalDateTime.parse("2024-04-30T10:15"),
        end = LocalDateTime.parse("2024-04-30T12:00"),
        description = "C502"
    ),
    Event(
        id = "532120",
        name = "IP komunikacije",
        shortName = "IK",
        color = accentBlue,
        colorId = 2131099687,
        professor = "Russo Mladen",
        eventType = TimetableType.PREDAVANJE,
        groups = "",
        classroom = "A105",
        start = LocalDateTime.parse("2024-04-30T12:15"),
        end = LocalDateTime.parse("2024-04-30T14:00"),
        description = "A105"
    ),
    Event(
        id = "538989",
        name = "Podržano strojno učenje",
        shortName = "PSU",
        color = accentRed,
        colorId = 2131100480,
        professor = "Vasilj Josip",
        eventType = TimetableType.LABORATORIJSKA_VJEZBA,
        groups = "Grupa 1,",
        classroom = "A507",
        start = LocalDateTime.parse("2024-05-02T10:00"),
        end = LocalDateTime.parse("2024-05-02T12:15"),
        description = "A507"
    ),
    Event(
        id = "535595",
        name = "Jezici i prevoditelji",
        shortName = "JIP",
        color = accentRed,
        colorId = 2131100480,
        professor = "Sikora Marjan",
        eventType = TimetableType.LABORATORIJSKA_VJEZBA,
        groups = "Grupa 1,",
        classroom = "B526",
        start = LocalDateTime.parse("2024-05-02T08:30"),
        end = LocalDateTime.parse("2024-05-02T10:00"),
        description = "B526"
    ),
    Event(
        id = "535336",
        name = "IP komunikacije",
        shortName = "IK",
        color = accentRed,
        colorId = 2131100480,
        professor = "Meter Davor",
        eventType = TimetableType.LABORATORIJSKA_VJEZBA,
        groups = "Grupa 1,",
        classroom = "B526",
        start = LocalDateTime.parse("2024-05-03T08:00"),
        end = LocalDateTime.parse("2024-05-03T09:30"),
        description = "B526"
    ),
)