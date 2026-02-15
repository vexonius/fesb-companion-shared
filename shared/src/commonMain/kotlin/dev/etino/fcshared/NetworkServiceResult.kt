package dev.etino.fcshared

import dev.etino.fcshared.attendance.models.AttendanceEntry

sealed class NetworkServiceResult {


    sealed class LogoutResult : NetworkServiceResult() {
        data class Success(val data: String) : LogoutResult()
        class Failure(val throwable: Throwable) : LogoutResult()
    }

    sealed class TimeTableResult : NetworkServiceResult() {
        data class Success(val data: String) : TimeTableResult()
        class Failure(val throwable: Throwable) : TimeTableResult()
    }

    sealed class IksicaResult : NetworkServiceResult() {
        data class Success(val data: String) : IksicaResult()
        class Failure(val throwable: Throwable) : IksicaResult()
    }

    sealed class AttendanceFetchResult : NetworkServiceResult() {
        class Success(val data: String) : AttendanceFetchResult()
        class Failure(throwable: Throwable) : AttendanceFetchResult()
    }

    sealed class AttendanceParseResult : NetworkServiceResult() {
        class Success(val data: List<List<AttendanceEntry>>) : AttendanceParseResult()
        class Failure(throwable: Throwable) : AttendanceParseResult()
    }

    sealed class WeatherResult : NetworkServiceResult() {
        data class Success(val data: String) : WeatherResult()
        class Failure(val exception: Throwable) : WeatherResult()
    }

    sealed class StudomatResult : NetworkServiceResult() {
        data class Success(val data: String) : StudomatResult()
        class Failure(val throwable: Throwable) : StudomatResult()
    }

    sealed class MenzaResult : NetworkServiceResult() {
        data class Success(val data: String) : MenzaResult()
        class Failure(exception: Throwable) : MenzaResult()
    }

}
