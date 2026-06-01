package dev.etino.fcshared.featuresKotlin.attendance.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import dev.etino.fcshared.featuresKotlin.attendance.models.AttendanceEntry

@Dao
interface AttendanceDao {
    @Query("DELETE FROM attendanceentry")
    suspend fun deleteAll()

    @Insert(onConflict = REPLACE)
    suspend fun insert(attendance: List<AttendanceEntry>)

    @Query("SELECT * FROM attendanceentry")
    suspend fun read(): List<AttendanceEntry>
}