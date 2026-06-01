package dev.etino.fcshared.featuresKotlin.home.repository

import dev.etino.fcshared.featuresKotlin.home.dao.NoteDao
import dev.etino.fcshared.featuresKotlin.home.models.Note
import dev.etino.fcshared.featuresKotlin.home.models.NoteRoom

class NoteRepository(private val noteDao: NoteDao) : NoteRepositoryInterface {

    override suspend fun getNotes(): List<Note> {
        return noteDao.getNotes().map { Note(it) }
    }

    override suspend fun insert(note: Note) {
        noteDao.insert(NoteRoom(note))
    }

    override suspend fun delete(note: Note) {
        noteDao.delete(NoteRoom(note))
    }

}