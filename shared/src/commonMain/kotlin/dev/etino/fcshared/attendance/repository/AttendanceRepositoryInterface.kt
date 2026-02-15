package dev.etino.fcshared.attendance.repository

import dev.etino.fcshared.attendance.models.AttendanceEntry
import dev.etino.fcshared.NetworkServiceResult

interface AttendanceRepositoryInterface {

    suspend fun fetchAttendance(): NetworkServiceResult.AttendanceParseResult

}
