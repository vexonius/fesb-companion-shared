package dev.etino.fcsharedtest

import dev.etino.fcshared.timetable.client.TimetableClientImpl
import kotlinx.coroutines.runBlocking
import org.testng.annotations.Test

@Test
class TimetableClientTest {

    fun testTimetableClient() {
        runBlocking {
            val client = TimetableClientImpl()

            val params: HashMap<String, String> = hashMapOf(
                "DataType" to "User",
                "DataId" to "sjurko00",
                "MinDate" to "02-03-2025",
                "MaxDate" to "02-08-2025"
            )

            val result = client.getTimetableEvents(params)
            assert(result.isNotEmpty())
        }
    }

}