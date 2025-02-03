package dev.etino.fcsharedtest

import dev.etino.fcshared.TimetableClientImpl
import kotlinx.coroutines.runBlocking
import org.testng.annotations.Test

@Test
class TimetableClientTest {

    fun testTimetableClient() {
        runBlocking {
            val client = TimetableClientImpl()
            val result = client.getTimetableEvents(emptyMap())
            println(result)

            assert(result.isNotEmpty())
        }
    }

}