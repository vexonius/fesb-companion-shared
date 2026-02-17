package dev.etino.fcshared.home.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import dev.etino.fcshared.home.models.NoteRoom


@Dao
interface NoteDao {
    @Query("SELECT * FROM noteroom")
    fun getNotes(): List<NoteRoom>

    @Insert(onConflict = REPLACE)
    fun insert(note: NoteRoom)

    @Delete
    fun delete(note: NoteRoom)
}
