package dev.etino.fcshared.attendance.services

import dev.etino.fcshared.NetworkServiceResult

interface AttendanceServiceInterface {

    suspend fun fetchAllAttendance(): NetworkServiceResult.AttendanceFetchResult

    suspend fun fetchAttendance(classId: String): NetworkServiceResult.AttendanceFetchResult

}
