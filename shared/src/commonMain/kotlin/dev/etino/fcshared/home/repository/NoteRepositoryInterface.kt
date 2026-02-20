package dev.etino.fcshared.home.repository

import dev.etino.fcshared.home.models.Note

interface NoteRepositoryInterface {

    suspend fun getNotes(): List<Note>

    suspend fun insert(note: Note)

    suspend fun delete(note: Note)

}
