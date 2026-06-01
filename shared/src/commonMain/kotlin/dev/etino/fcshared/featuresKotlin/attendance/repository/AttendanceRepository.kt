package dev.etino.fcshared.featuresKotlin.attendance.repository


import dev.etino.fcshared.featuresKotlin.attendance.ParseAttendance
import dev.etino.fcshared.featuresKotlin.attendance.dao.AttendanceDao
import dev.etino.fcshared.featuresKotlin.attendance.models.AttendanceEntry
import dev.etino.fcshared.featuresKotlin.attendance.services.AttendanceServiceInterface
import dev.etino.fcshared.featuresKotlin.networking.NetworkServiceResult
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class AttendanceRepository(
    private val attendanceService: AttendanceServiceInterface,
    private val attendanceDao: AttendanceDao,
    private val parseAttendance: ParseAttendance = ParseAttendance()
) : AttendanceRepositoryInterface {

    override suspend fun fetchAttendance(): NetworkServiceResult.AttendanceParseResult {
        when (val list = attendanceService.fetchAllAttendance()) {
            is NetworkServiceResult.AttendanceFetchResult.Success -> {
                val attendanceList: List<List<AttendanceEntry>> = coroutineScope {
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
                    insertAttendance(attendanceList.flatten())
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

    override suspend fun insertAttendance(attendance: List<AttendanceEntry>) {
        attendanceDao.deleteAll()
        attendanceDao.insert(attendance)
    }

    override suspend fun readAttendance(): List<List<AttendanceEntry>> {
        val test = attendanceDao.read()
            .groupBy { it.subject }.values
            .toList()
            .sortedByClassAndSemester()
        return test
    }
}

fun List<List<AttendanceEntry>>.sortedByClassAndSemester() = sortedBy { it.firstOrNull()?.subject }
    .sortedBy { it.firstOrNull()?.semester }