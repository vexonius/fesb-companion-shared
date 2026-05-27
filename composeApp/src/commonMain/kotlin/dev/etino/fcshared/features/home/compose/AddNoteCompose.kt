package dev.etino.fcshared.features.home.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.etino.fcshared.compose.AppTheme
import dev.etino.fcshared.home.models.Note
import dev.etino.fcshared.now
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.add_note
import fesb_companion_shared.composeapp.generated.resources.cancel_note
import fesb_companion_shared.composeapp.generated.resources.enter_note
import fesb_companion_shared.composeapp.generated.resources.note_add
import fesb_companion_shared.composeapp.generated.resources.note_cancel
import fesb_companion_shared.composeapp.generated.resources.note_save_button
import fesb_companion_shared.composeapp.generated.resources.save_note
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@OptIn(ExperimentalUuidApi::class)
@Composable
fun AddNoteCompose(insertNote: (note: Note) -> Unit) {
    val openDialog = remember { mutableStateOf(false) }
    val iconSize = Dp(MaterialTheme.typography.bodyMedium.lineHeight.value)
    val height = 28.dp
    var measuredHeight: MutableIntState = remember { mutableIntStateOf(0) }

    Spacer(Modifier.height(8.dp))
    if (!openDialog.value) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .onGloballyPositioned{ measuredHeight.intValue = it.size.height }
                .fillMaxWidth()
                .padding(vertical = 2.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable { openDialog.value = true }
                .padding(4.dp, 4.dp, 8.dp, 4.dp)) {
            Icon(
                painter = painterResource(Res.drawable.note_add),
                contentDescription = stringResource(Res.string.add_note),
                modifier = Modifier.size(iconSize),
            )
            Text(
                text = stringResource(Res.string.add_note),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 10.dp),
            )
        }
    } else {
        val editMessage = remember { mutableStateOf("") }
        val message = remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) { focusRequester.requestFocus() }
        fun submit() {
            message.value = editMessage.value
            openDialog.value = false
            if (message.value.isEmpty()) return
            insertNote(
                Note(
                    noteTekst = message.value,
                    checked = false,
                    dateCreated = LocalDateTime.now(),
                    id = Uuid.generateV4().toString(),
                )
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(with(LocalDensity.current) { measuredHeight.intValue.toDp() })
                .fillMaxWidth()
        ) {
            BasicTextField(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .weight(0.5f),
                value = editMessage.value,
                onValueChange = { editMessage.value = it },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                cursorBrush = SolidColor(Color.White),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { submit() }),
                decorationBox = { innerTextField ->
                    Row(
                        Modifier
                            .height(height)
                            .padding(start = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.weight(1f)) {
                            if (editMessage.value.isEmpty()) {
                                Text(
                                    text = stringResource(Res.string.enter_note),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                            innerTextField()
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                painter = painterResource(Res.drawable.note_cancel),
                contentDescription = stringResource(Res.string.cancel_note),
                modifier = Modifier
                    .size(height)
                    .noRippleClickable { openDialog.value = false },
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                painter = painterResource(Res.drawable.note_save_button),
                contentDescription = stringResource(Res.string.save_note),
                modifier = Modifier
                    .size(height)
                    .noRippleClickable { submit() },
            )
        }
    }
}

@Preview
@Composable
fun AddNotePreview() {
    AppTheme() {
        Surface {
            AddNoteCompose { }
        }
    }
}