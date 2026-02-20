package dev.etino.fcshared.home.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

data class Note(
    val id: String,
    val noteTekst: String,
    val dateCreated: LocalDateTime,
    var checked: Boolean
) {
    constructor(noteRoom: NoteRoom) : this(
        id = noteRoom.id,
        noteTekst = noteRoom.noteTekst,
        dateCreated = noteRoom.dateCreated.let {
            try {
                LocalDateTime.parse(it)
            } catch (e: IllegalArgumentException) {
                LocalDateTime(LocalDate.fromEpochDays(0), LocalTime.fromSecondOfDay(0))
            }
        },
        checked = noteRoom.checked
    )
}

@Entity
open class NoteRoom(
    @PrimaryKey
    var id: String,
    var noteTekst: String,
    var dateCreated: String,
    var checked: Boolean
) {
    constructor(note: Note) : this(
        id = note.id,
        noteTekst = note.noteTekst,
        dateCreated = note.dateCreated.toString(),
        checked = note.checked
    )
}