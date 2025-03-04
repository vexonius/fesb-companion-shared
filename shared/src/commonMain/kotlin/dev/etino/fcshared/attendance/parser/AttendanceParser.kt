package dev.etino.fcshared.attendance.parser

import com.fleeksoft.ksoup.Ksoup
import dev.etino.fcshared.Utils.Parsable
import dev.etino.fcshared.attendance.models.AttendanceItemResponse
import dev.etino.fcshared.attendance.models.AttendanceTableItemResponse

class AttendanceParser : Parsable<String, List<AttendanceTableItemResponse>> {

    override fun parse(data: String): List<AttendanceTableItemResponse> {
        val doc = Ksoup.parse(data)

        val winterSemesterItems = doc.select("div.semster.winter div.body.clearfix a")
            .map { element ->
                    val className = element.select(".cellContent").firstOrNull()?.text() ?: ""
                    val url = element.attr("href")

                    AttendanceTableItemResponse(className, url, 1)
            }

        val summerSemesterItems = doc.select("div.semster.summer div.body.clearfix a")
            .map { element ->
                val className = element.select(".cellContent").firstOrNull()?.text() ?: ""
                val url = element.attr("href")

                AttendanceTableItemResponse(className, url, 2)
            }

        return winterSemesterItems + summerSemesterItems
    }

    fun parseItem(data: String, `class`: String, semester: Int): List<AttendanceItemResponse> {
        val items = Ksoup.parse(data)
            .select(".courseCategories div.courseCategory")
            .map { item ->
                val type = (item.getElementsByClass("name").first()?.text() ?: "")
                    .replaceFirstChar { it.uppercase() }
                val attended = item.select(".attended > span.num").first()?.text()
                    ?.toInt() ?: -1
                val absent = item.select(".absent > span.num").first()?.text()
                    ?.toInt() ?: -1
                val reqAttend = item.select(".required-attendance > span").first()?.text()
                val required = (reqAttend?.split("od")?.firstOrNull()?.trim() ?: "")
                    .toIntOrNull() ?: -1
                val total = (reqAttend?.split("od")?.last()?.trim())?.toIntOrNull() ?: -1
                val id = "${attended}${`class`}${absent}${type}${required}${total}${semester}"

                AttendanceItemResponse(id, `class`, type, attended, absent, required, semester, total)
            }

        return items
    }

}