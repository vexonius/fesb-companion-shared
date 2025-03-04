package dev.etino.fcsharedtest

import dev.etino.fcshared.attendance.client.AttendanceClientImpl
import dev.etino.fcshared.attendance.parser.AttendanceParser
import dev.etino.fcshared.attendance.repository.AttendanceRepositoryImpl
import dev.etino.fcshared.user.UserServiceImpl
import kotlinx.coroutines.runBlocking
import org.testng.annotations.Test

@Test
class AttendanceTest {

    fun testAttendanceFetching() {
        val attendanceClient = AttendanceClientImpl()
        val parser = AttendanceParser()
        val attendanceRepository = AttendanceRepositoryImpl(attendanceClient, parser)

        runBlocking {
            val userService = UserServiceImpl()
            val loginServiceResult = userService.login("", "!")

            try {
                val result = attendanceRepository.getAttendance()
                assert(result.isNotEmpty())
            } catch (e: Exception) {
                print(e)
            }
        }
    }

}