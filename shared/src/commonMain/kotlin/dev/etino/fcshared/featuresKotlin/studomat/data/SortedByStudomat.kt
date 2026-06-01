package dev.etino.fcshared.featuresKotlin.studomat.data

import dev.etino.fcshared.featuresKotlin.studomat.models.StudomatSubject

fun List<StudomatSubject>.sortedByNameAndSemester(): List<StudomatSubject> {
    return this
        .sortedBy { it.name }
        .sortedBy { it.semester }
}