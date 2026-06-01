package dev.etino.fcshared.featuresKotlin.attendance.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AttendanceEntry(
    @PrimaryKey
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

