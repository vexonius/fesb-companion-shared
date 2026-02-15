package dev.etino.fcshared.attendance.services

import dev.etino.fcshared.networking.NetworkServiceResult

interface AttendanceServiceInterface {

    suspend fun fetchAllAttendance(): NetworkServiceResult.AttendanceFetchResult

    suspend fun fetchAttendance(classId: String): NetworkServiceResult.AttendanceFetchResult

}
