package dev.etino.fcshared.attendance.models

data class AttendanceItemResponse(
    var id: String,
    var `class`: String,
    var type: String,
    var attended: Int,
    var absent: Int,
    var required: Int,
    var semester: Int,
    var total: Int
)