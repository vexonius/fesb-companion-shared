package dev.etino.fcshared.studomat.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import dev.etino.fcshared.studomat.models.StudomatSubject
import dev.etino.fcshared.studomat.models.StudomatYearInfo

@Dao
interface StudomatDao {
    @Query("DELETE FROM studomatyearinfo")
    suspend fun deleteYears()
    @Query("DELETE FROM StudomatSubject")
    suspend fun deleteAllSubjects()

    @Query("DELETE FROM StudomatSubject WHERE year = :year")
    suspend fun deleteAll(year: String)

    @Insert(onConflict = REPLACE)
    suspend fun insertYears(years: List<StudomatYearInfo>)

    @Insert(onConflict = REPLACE)
    suspend fun insert(subjects: List<StudomatSubject>)

    @Query("SELECT * FROM studomatyearinfo")
    suspend fun readYears(): List<StudomatYearInfo>

    @Query("SELECT * FROM StudomatSubject")
    suspend fun read(): List<StudomatSubject>

}