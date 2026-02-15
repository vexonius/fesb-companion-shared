package dev.etino.fcshared.attendance.repository


import dev.etino.fcshared.attendance.models.AttendanceEntry
import dev.etino.fcshared.attendance.services.AttendanceServiceInterface
import dev.etino.fcshared.NetworkServiceResult
import dev.etino.fcshared.attendance.ParseAttendance
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

class AttendanceRepository(
    private val attendanceService: AttendanceServiceInterface,
    private val parseAttendance: ParseAttendance = ParseAttendance()
) : AttendanceRepositoryInterface {

    override suspend fun fetchAttendance(): NetworkServiceResult.AttendanceParseResult {
        when (val list = attendanceService.fetchAllAttendance()) {
            is NetworkServiceResult.AttendanceFetchResult.Success -> {
                val attendanceList: List<List<AttendanceEntry>> = runBlocking {
                    parseAttendance.parseAttendList(list.data).map {
                        async {
                            //Log.d("AttendanceRepository", "Fetching attendance for ${it.first.text()}")
                            when (val classData = attendanceService.fetchAttendance(it.first.attr("href"))) {
                                is NetworkServiceResult.AttendanceFetchResult.Success -> {
                                    parseAttendance.parseAttendance(
                                        it.first,
                                        classData.data,
                                        it.second
                                    )
                                }

                                is NetworkServiceResult.AttendanceFetchResult.Failure -> {
                                    emptyList()
                                }
                            }
                        }
                    }
                }.awaitAll().filter { it.isNotEmpty() }


                return if (attendanceList.isEmpty()) {
                    NetworkServiceResult.AttendanceParseResult.Failure(
                        Throwable("Error while fetching attendance data")
                    )
                } else {
                    NetworkServiceResult.AttendanceParseResult.Success(
                        attendanceList.sortedByClassAndSemester()
                    )
                }
            }

            is NetworkServiceResult.AttendanceFetchResult.Failure -> {
                return NetworkServiceResult.AttendanceParseResult.Failure(
                    Throwable("Error while fetching attendance data")
                )
            }
        }
    }
}

fun List<List<AttendanceEntry>>.sortedByClassAndSemester() = sortedBy { it.firstOrNull()?.subject }
    .sortedBy { it.firstOrNull()?.semester }