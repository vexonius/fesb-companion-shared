package dev.etino.fcshared.featuresKotlin.studomat.models


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class StudomatYearInfo {
    @PrimaryKey
    var id: String = ""
    var courseName: String = ""
    var studyProgram: String = ""
    var parallelStudy: String = ""
    var yearOfCourse: Int = 0
    var enrollmentIndicator: String = ""
    var payment: Boolean = false
    var fundingBasis: String = ""
    var universityCenter: String = ""
    var studentRightsValidUntil: String = ""
    var enrollmentDate: String = ""
    var enrollmentCompleted: Boolean = false
    var academicYear: String = ""
    var href: String = ""
}