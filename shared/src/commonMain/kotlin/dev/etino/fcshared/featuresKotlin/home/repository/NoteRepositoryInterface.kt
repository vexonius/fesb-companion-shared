package dev.etino.fcshared.featuresKotlin.home.repository

import dev.etino.fcshared.featuresKotlin.home.models.Note

interface NoteRepositoryInterface {

    suspend fun getNotes(): List<Note>

    suspend fun insert(note: Note)

    suspend fun delete(note: Note)

}
