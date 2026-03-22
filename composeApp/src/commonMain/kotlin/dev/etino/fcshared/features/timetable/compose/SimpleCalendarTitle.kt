package dev.etino.fcshared.features.timetable.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.etino.fcshared.compose.contentColors
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.ic_chevron_left
import fesb_companion_shared.composeapp.generated.resources.ic_chevron_right
import fesb_companion_shared.composeapp.generated.resources.lastMonth
import fesb_companion_shared.composeapp.generated.resources.nextMonth
import kotlinx.datetime.YearMonth
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SimpleCalendarTitle(
    modifier: Modifier,
    currentMonth: YearMonth,
    goToPrevious: () -> Unit,
    goToNext: () -> Unit,
) {
    Row(
        modifier = modifier.height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CalendarNavigationIcon(
            icon = painterResource(Res.drawable.ic_chevron_left),
            contentDescription = stringResource(Res.string.lastMonth),
            onClick = goToPrevious,
        )
        Text(
            modifier = Modifier
                .weight(1f),
            text = monthNameHr(currentMonth.month.ordinal)
                    + " " + currentMonth.year.toString(),
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.contentColors.primary
        )
        CalendarNavigationIcon(
            icon = painterResource(Res.drawable.ic_chevron_right),
            contentDescription = stringResource(Res.string.nextMonth),
            onClick = goToNext,
        )
    }
}

fun monthNameHr(month: Int): String =
    when (month) {
       0 -> "Siječanj"
       1 -> "Veljača"
       2 -> "Ožujak"
       3 -> "Travanj"
       4 -> "Svibanj"
       5 -> "Lipanj"
       6 -> "Srpanj"
       7 -> "Kolovoz"
       8 -> "Rujan"
       9  -> "Listopad"
       10 -> "Studeni"
       11 -> "Prosinac"
       else -> ""
    }

@Composable
private fun CalendarNavigationIcon(
    icon: Painter,
    contentDescription: String,
    onClick: () -> Unit,
) = Box(
    modifier = Modifier
        .fillMaxHeight()
        .aspectRatio(1f)
        .clip(shape = CircleShape)
        .clickable(role = Role.Button, onClick = onClick),
) {
    Icon(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .align(Alignment.Center),
        painter = icon,
        contentDescription = contentDescription,
    )
}
