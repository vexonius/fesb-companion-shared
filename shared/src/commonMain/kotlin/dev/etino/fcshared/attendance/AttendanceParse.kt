package dev.etino.fcshared.attendance

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Element
import dev.etino.fcshared.attendance.models.AttendanceEntry
import io.ktor.utils.io.core.toByteArray
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ParseAttendance {

@OptIn(ExperimentalUuidApi::class)
fun parseAttendance(
    element: Element,
    body: String,
    semester: Int
): List<AttendanceEntry> {
    val attendanceForOneSemester = mutableListOf<AttendanceEntry>()
    Ksoup.parse(body).select(".courseCategories div.courseCategory").forEach { kat ->
        val mAttendanceEntry = AttendanceEntry()
        mAttendanceEntry.semester = semester
        mAttendanceEntry.subject = element.select(".cellContent").first()?.text() ?: ""
        mAttendanceEntry.type =
            (kat.getElementsByClass("name").first()?.text() ?: "").replaceFirstChar { it.uppercase() }
        mAttendanceEntry.attended = kat.select(".attended > span.num").first()?.text()?.toInt() ?: -1
        mAttendanceEntry.absent = kat.select(".absent > span.num").first()?.text()?.toInt() ?: -1
        val reqAttend = kat.select(".required-attendance > span").first()?.text()
        mAttendanceEntry.required = (reqAttend?.split("od")?.firstOrNull()?.trim() ?: "").toIntOrNull() ?: -1
        mAttendanceEntry.total = (reqAttend?.split("od")?.last()?.trim())?.toIntOrNull() ?: -1
        mAttendanceEntry.id = Uuid.random().toString()
        attendanceForOneSemester.add(mAttendanceEntry)
    }

    return attendanceForOneSemester
}

fun parseAttendList(body: String): List<Pair<Element, Int>> {
    val doc = body.let { Ksoup.parse(it) }
    val attendanceUrls: MutableList<Pair<Element, Int>> = mutableListOf()
    try {
        attendanceUrls.addAll(
            doc.select("div.semster.winter div.body.clearfix a").map { element -> Pair(element, 1) })
        attendanceUrls.addAll(
            doc.select("div.semster.summer div.body.clearfix a").map { element -> Pair(element, 2) })
    } catch (ex: Exception) {
        //ex.message?.let { Log.d("Parsing Attendance data failed.", it) }
        ex.printStackTrace()
    }
    return attendanceUrls
}
}