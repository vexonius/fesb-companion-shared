package dev.etino.fcshared.featuresKotlin.attendance.repository

import dev.etino.fcshared.featuresKotlin.attendance.models.AttendanceEntry
import dev.etino.fcshared.featuresKotlin.networking.NetworkServiceResult

interface AttendanceRepositoryInterface {

    suspend fun fetchAttendance(): NetworkServiceResult.AttendanceParseResult

    suspend fun insertAttendance(attendance: List<AttendanceEntry>)

    suspend fun readAttendance(): List<List<AttendanceEntry>>

}
