package dev.etino.fcshared.timetable.models

enum class TimetableTypeResponse(val value: String) {
    CLASSES("Predavanja"),
    AUDITORY_CLASSES("Auditorne vježbe"),
    COLLOQUIUMS("Kolokviji"),
    LABS("Laboratorijske vježbe"),
    CONSTRUCTS("Konstrukcijske vježbe"),
    SEMINARS("Seminari"),
    EXAMS("Ispiti"),
    OTHER("Other")
}