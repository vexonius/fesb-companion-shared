package dev.etino.fcshared.attendance.repository

import dev.etino.fcshared.networking.NetworkServiceResult

interface AttendanceRepositoryInterface {

    suspend fun fetchAttendance(): NetworkServiceResult.AttendanceParseResult

}
