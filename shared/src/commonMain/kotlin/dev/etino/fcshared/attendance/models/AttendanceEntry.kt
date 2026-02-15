package dev.etino.fcshared.attendance.models

data class AttendanceEntry(
    var id: String = "",
    var subject: String = "",
    var type: String = "",
    var link: String = "",
    var attended: Int = 0,
    var absent: Int = 0,
    var required: Int = 0,
    var semester: Int = 0,
    var total: Int = 0,
)
