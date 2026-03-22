package dev.etino.fcshared.features.home.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.etino.fcshared.compose.notesContainer
import dev.etino.fcshared.home.models.Note
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.notes
import org.jetbrains.compose.resources.stringResource

@Composable
fun NotesCompose(
    notes: List<Note>,
    insertNote: (note: Note) -> Unit,
    deleteNote: (note: Note) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(24.dp, 12.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(notesContainer)
            .padding(20.dp, 12.dp)
            .animateContentSize()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Row {
            Text(
                text = stringResource(Res.string.notes),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 5.dp)
            )
        }
        AddNoteCompose(insertNote)
        notes.sortedByDescending { it.dateCreated }.forEach { note ->
            key(note.id) {
                NoteItem(
                    note = note,
                    delete = { deleteNote(note) },
                    markDone = { isDone ->
                        insertNote(note.apply { checked = isDone })
                    }
                )
            }
        }
    }
}