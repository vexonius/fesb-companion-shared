package dev.etino.fcshared.featuresKotlin.attendance.services

import dev.etino.fcshared.featuresKotlin.networking.NetworkServiceResult

interface AttendanceServiceInterface {

    suspend fun fetchAllAttendance(): NetworkServiceResult.AttendanceFetchResult

    suspend fun fetchAttendance(classId: String): NetworkServiceResult.AttendanceFetchResult

}
