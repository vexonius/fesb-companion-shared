package dev.etino.fcshared.studomat.data

import dev.etino.fcshared.studomat.models.StudomatSubject

fun List<StudomatSubject>.sortedByNameAndSemester(): List<StudomatSubject> {
    return this
        .sortedBy { it.name }
        .sortedBy { it.semester }
}