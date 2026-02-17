package dev.etino.fcshared.screens.home.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.etino.fcshared.compose.AppTheme
import dev.etino.fcshared.home.models.Note
import dev.etino.fcshared.screens.home.compose.NoteItemState.Default
import dev.etino.fcshared.screens.home.compose.NoteItemState.Edit
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.checkmark_note_desc
import fesb_companion_shared.composeapp.generated.resources.delete_note_desc
import fesb_companion_shared.composeapp.generated.resources.note_checkmark
import fesb_companion_shared.composeapp.generated.resources.note_circle
import fesb_companion_shared.composeapp.generated.resources.note_delete
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    note: Note,
    markDone: (isDone: Boolean) -> Unit,
    delete: () -> Unit
) {
    val isDone = remember { mutableStateOf(note.checked == true) }
    val noteItemState: MutableState<NoteItemState> = remember { mutableStateOf(Default) }
    val iconSize = Dp(MaterialTheme.typography.bodyMedium.lineHeight.value)

    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .padding(vertical = 2.dp)
            .clip(RoundedCornerShape(12.dp))
            .combinedClickable(onLongClick = {
                noteItemState.value = noteItemState.value.switch()
            }) {}
            .padding(4.dp, 4.dp, 8.dp, 4.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        when (noteItemState.value) {
            Edit -> {
                Icon(
                    painter = painterResource(Res.drawable.note_delete),
                    contentDescription = stringResource(Res.string.delete_note_desc),
                    modifier = Modifier
                        .size(iconSize)
                        .noRippleClickable {
                            noteItemState.value = noteItemState.value.switch()
                            delete()
                        }
                )
            }

            Default -> {
                Icon(
                    painter = painterResource(if (isDone.value) Res.drawable.note_checkmark else Res.drawable.note_circle),
                    contentDescription = stringResource(Res.string.checkmark_note_desc),
                    modifier = Modifier
                        .size(iconSize)
                        .noRippleClickable {
                            isDone.value = !isDone.value
                            markDone(isDone.value)
                        }
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = iconSize)
        ) {
            Text(
                text = note.noteTekst,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 10.dp),
                textDecoration = if (isDone.value) TextDecoration.LineThrough else TextDecoration.None
            )
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Preview
@Composable
fun NoteItemPreview() {
    AppTheme {
        Surface {
            NoteItem(
                note = Note(
                    id = Uuid.generateV4().toString(),
                    noteTekst = "Test",
                    dateCreated = LocalDateTime.parse("2024-04-29T12:00"),
                    checked = false
                ),
                markDone = {},
                delete = {}
            )
        }
    }
}

enum class NoteItemState {
    Default, Edit;

    fun switch(): NoteItemState {
        return when (this) {
            Default -> Edit
            Edit -> Default
        }
    }

}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}
