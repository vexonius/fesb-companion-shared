package dev.etino.fcshared.attendance.client

interface AttendanceClient {

    suspend fun getAttendanceItems(): String

    suspend fun getAttendanceItem(resourcePath: String): String

}