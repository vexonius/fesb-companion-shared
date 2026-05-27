package dev.etino.fcshared.attendance.repository

import dev.etino.fcshared.attendance.models.AttendanceEntry
import dev.etino.fcshared.networking.NetworkServiceResult

interface AttendanceRepositoryInterface {

    suspend fun fetchAttendance(): NetworkServiceResult.AttendanceParseResult

    suspend fun insertAttendance(attendance: List<AttendanceEntry>)

    suspend fun readAttendance(): List<List<AttendanceEntry>>

}
