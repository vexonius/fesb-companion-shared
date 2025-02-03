package dev.etino.fcshared.timetable.models

enum class TimetableType(val value: String) {
    PREDAVANJE("Predavanja"),
    AUDITORNA_VJEZBA("Auditorne vježbe"),
    KOLOKVIJ("Kolokviji"),
    LABORATORIJSKA_VJEZBA("Laboratorijske vježbe"),
    KONSTRUKCIJSKA_VJEZBA("Konstrukcijske vježbe"),
    SEMINAR("Seminari"),
    ISPIT("Ispiti"),
    OTHER("Other")
}