package dev.etino.fcshared.featuresKotlin.home.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import dev.etino.fcshared.featuresKotlin.home.models.NoteRoom


@Dao
interface NoteDao {

    @Query("DELETE FROM noteroom")
    suspend fun deleteAll()

    @Query("SELECT * FROM noteroom")
    suspend fun getNotes(): List<NoteRoom>

    @Insert(onConflict = REPLACE)
    suspend fun insert(note: NoteRoom)

    @Delete
    suspend fun delete(note: NoteRoom)
}
