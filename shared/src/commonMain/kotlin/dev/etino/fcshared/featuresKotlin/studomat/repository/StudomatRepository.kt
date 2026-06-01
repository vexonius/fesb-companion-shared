package dev.etino.fcshared.featuresKotlin.studomat.repository

import dev.etino.fcshared.featuresKotlin.networking.NetworkServiceResult
import dev.etino.fcshared.featuresKotlin.studomat.dao.StudomatDao
import dev.etino.fcshared.featuresKotlin.studomat.data.parseCurrentYear
import dev.etino.fcshared.featuresKotlin.studomat.data.parseStudent
import dev.etino.fcshared.featuresKotlin.studomat.data.parseYears
import dev.etino.fcshared.featuresKotlin.studomat.data.sortedByNameAndSemester
import dev.etino.fcshared.featuresKotlin.studomat.models.StudomatYear
import dev.etino.fcshared.featuresKotlin.studomat.models.StudomatYearInfo
import dev.etino.fcshared.featuresKotlin.studomat.repository.models.StudomatRepositoryResult
import dev.etino.fcshared.featuresKotlin.studomat.services.StudomatService

class StudomatRepository(
    private val studomatService: StudomatService,
    private val studomatDao: StudomatDao,
) {

    suspend fun getStudomatDataAndYears(): StudomatRepositoryResult.StudentAndYearsResult {
        val student = parseStudent(studomatService.getStudomatData())

        return when (val result = studomatService.getYearNames()) {
            is NetworkServiceResult.StudomatResult.Success -> {
                val resultGetYears = parseYears(result.data)
                //Log.d("StudomatRepository", "getYears: $resultGetYears")
                StudomatRepositoryResult.StudentAndYearsResult.Success(resultGetYears, student)
            }

            is NetworkServiceResult.StudomatResult.Failure -> {
                //Log.d("StudomatRepository", "getYears: ${result.throwable.message}")
                StudomatRepositoryResult.StudentAndYearsResult.Failure("Failure getting data:${result.throwable.message}")
            }
        }
    }

    suspend fun getYear(year: StudomatYearInfo): StudomatRepositoryResult.ChosenYearResult {
        return when (val data = studomatService.getYearSubjects(year.href)) {
            is NetworkServiceResult.StudomatResult.Success -> {
                val parsedSubjects = parseCurrentYear(data.data, year)
                //Log.d("StudomatRepository", "getOdabranuGodinu: $parsedSubjects")
                StudomatRepositoryResult.ChosenYearResult.Success(parsedSubjects)
            }

            is NetworkServiceResult.StudomatResult.Failure -> {
                //Log.d("StudomatRepository", "getOdabranuGodinu: ${data.throwable.message}")
                StudomatRepositoryResult.ChosenYearResult.Failure("Failure getting data:${data.throwable.message}")
            }
        }
    }

    suspend fun insert(year: StudomatYear) {
        year.subjects.firstOrNull()?.year?.let { studomatDao.deleteAll(it) }
        studomatDao.insert(year.subjects)
        studomatDao.insertYears(listOf(year.yearInfo))
    }

    suspend fun readData(): List<StudomatYear> {
        val years = studomatDao.readYears().sortedBy { it.academicYear }
        val subjects = studomatDao.read().sortedByNameAndSemester().groupBy { it.year }
        return years.mapNotNull { yearInfo ->
            subjects[yearInfo.academicYear]?.let { subjectsForYearAndCourse ->
                StudomatYear(yearInfo, subjectsForYearAndCourse)
            }
        }
    }
}