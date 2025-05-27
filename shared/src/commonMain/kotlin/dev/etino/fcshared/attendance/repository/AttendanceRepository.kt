package dev.etino.fcshared.attendance.repository

import dev.etino.fcshared.attendance.models.AttendanceItemResponse

interface AttendanceRepository {

    @Throws(Exception::class)
    suspend fun getAttendance(): List<AttendanceItemResponse>

}