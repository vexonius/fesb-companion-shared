package dev.etino.fcshared.studomat.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class StudomatSubject(
    @PrimaryKey
    var id: String = "",
    var name: String = "",
    var electiveGroup: String = "",
    var semester: String = "",
    var lectures: String = "",
    var exercises: String = "",
    var ectsEnrolled: String = "",
    var isTaken: String = "",
    var status: String = "",
    var grade: String = "",
    var examDate: String = "",
    var year: String = "",
    var course: String = "",
    var isPassed: Boolean = false
)
