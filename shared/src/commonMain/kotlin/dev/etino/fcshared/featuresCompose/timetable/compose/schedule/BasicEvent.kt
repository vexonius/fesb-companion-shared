package dev.etino.fcshared.featuresCompose.timetable.compose.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.etino.fcshared.featuresKotlin.timetable.Event

@Composable
fun BasicEvent(
    positionedEvent: PositionedEvent, modifier: Modifier = Modifier, onClick: (Event) -> Unit = {}
) {
    val event = positionedEvent.event
    val topRadius =
        if (positionedEvent.splitType == SplitType.Companion.Start || positionedEvent.splitType == SplitType.Companion.Both) 0.dp else 8.dp
    val bottomRadius =
        if (positionedEvent.splitType == SplitType.Companion.End || positionedEvent.splitType == SplitType.Companion.Both) 0.dp else 8.dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(2.dp)
            .clipToBounds()
            .background(
                event.color,
                shape = RoundedCornerShape(
                    topStart = topRadius,
                    topEnd = topRadius,
                    bottomEnd = bottomRadius,
                    bottomStart = bottomRadius,
                )
            )
            .padding(4.dp)
            .clickable { onClick(positionedEvent.event) }) {
        Text(
            text = event.name,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false),
        )

        Text(
            text = event.classroom,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .weight(1f),
        )
    }
}
