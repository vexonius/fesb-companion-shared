package dev.etino.fcshared.studomat.data

import com.fleeksoft.ksoup.Ksoup
import dev.etino.fcshared.studomat.models.Student
import dev.etino.fcshared.studomat.models.StudomatSubject
import dev.etino.fcshared.studomat.models.StudomatYearInfo

fun parseYears(body: String): List<StudomatYearInfo> {
    val data = Ksoup.parse(body)
    val listOfYears = data.select(".price-table__item").map { element ->
        val hrefTemp = element.selectFirst("a[title=Prikaži podatke o upisu]")?.attr("href") ?: ""
        StudomatYearInfo().apply {
            id = hrefTemp.split("/").lastOrNull().toString()
            studyProgram = element.select(".price-table__title p").getOrNull(1)?.text() ?: ""
            academicYear = element.select(".price-table__title p").getOrNull(0)?.text() ?: ""
            href = element.selectFirst("a[title=Prikaži podatke o upisu]")?.attr("href") ?: ""
        }
    }
    return listOfYears
}

fun parseStudent(body: String): Student {
    val data = Ksoup.parse(body)
    return Student(
        name = data.selectFirst(".user__name")?.text()?.substringBefore(" ") ?: "",
        surname = data.selectFirst(".user__name")?.text()?.substringAfter(" ") ?: "",
        jmbag = data.selectFirst(".user__email")?.text()?.substringBefore(" ") ?: "",
    )
}

fun parseCurrentYear(body: String, yearInfo: StudomatYearInfo): Pair<StudomatYearInfo, List<StudomatSubject>> {
    val data = Ksoup.parse(body)
    val course = data.selectFirst(".podnaslovStudiranje")?.text() ?: ""
    val table = data.select(".card-block .table.table-bordered tbody tr")

    yearInfo.apply {
        courseName = course
        studyProgram = table.select("td:contains(Upisani studij(i)) + td").text()
        parallelStudy = table.select("td:contains(Paralelni studij) + td").text()
        yearOfCourse = table.select("td:contains(Nastavna godina) + td").text().toIntOrNull() ?: 0
        enrollmentIndicator = table.select("td:contains(Indikator upisa) + td").text()
        payment = table.select("td:contains(Plaćanje) + td").text() == "Da"
        fundingBasis = table.select("td:contains(Temelj financiranja) + td").text()
        universityCenter = table.select("td:contains(Centar visokog učilišta) + td").text()
        studentRightsValidUntil = table.select("td:contains(Studentska prava vrijede do) + td").text()
        enrollmentDate = table.select("td:contains(Datum upisa) + td").text()
        enrollmentCompleted = table.select("td:contains(Upis obavljen u cijelosti) + td").text() == "Da"
    }


    val list: List<StudomatSubject> = data.select(".responsive-table tbody tr").map { tr ->
        val name = tr.selectFirst("td[data-title=Naziv predmeta:]")?.text() ?: ""
        val year = data.title().substringAfter("godinu ")
        val semester = tr.selectFirst("td[data-title=Semestar:]")?.text() ?: ""
        StudomatSubject(
            id = year + name + semester,
            name = name,
            electiveGroup = tr.selectFirst("td[data-title=Izborna grupa:]")?.text() ?: "",
            semester = semester,
            lectures = tr.selectFirst("td[data-title=Predavanja:]")?.text() ?: "",
            exercises = tr.selectFirst("td[data-title=Vježbe:]")?.text() ?: "",
            ectsEnrolled = tr.selectFirst("td[data-title=ECTS upisano:]")?.text() ?: "",
            isTaken = tr.selectFirst("td[data-title=Polaže se:]")?.text() ?: "",
            status = tr.selectFirst("td[data-title=Status:]")?.text() ?: "",
            grade = tr.selectFirst("td[data-title=Ocjena:]")?.text() ?: "",
            examDate = tr.selectFirst("td[data-title=Datum ispitnog roka:]")?.text() ?: "",
            year = year,
            course = course,
            isPassed = (tr.selectFirst("td[data-title=Ocjena:]")?.text() ?: "") in listOf("2", "3", "4", "5"),
        )
    }
    return Pair(yearInfo, list)
}