package dev.etino.fcshared.featuresKotlin.studomat.models

data class Exam(
    var name: String = "",
    var date: String = "",
    var time: String = "",
    var registerUntilDate: String = "",
    var registerUntilTime: String = "",
    var unregisterUntilDate: String = "",
    var unregisterUntilTime: String = "",
    var type: String = "",
    var description: String = "",
    var totalAttendances: String = "",
    var attendancesThisYear: String = ""
)
