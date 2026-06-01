package dev.etino.fcshared.featuresCompose.studomat.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.etino.fcshared.compose.passGreen
import dev.etino.fcshared.featuresCompose.home.compose.noRippleClickable
import dev.etino.fcshared.featuresKotlin.studomat.models.StudomatSubject
import fesb_companion_shared.shared.generated.resources.Res
import fesb_companion_shared.shared.generated.resources.ects_enrolled
import fesb_companion_shared.shared.generated.resources.elective_group
import fesb_companion_shared.shared.generated.resources.enrolled_subjects
import fesb_companion_shared.shared.generated.resources.exam_date
import fesb_companion_shared.shared.generated.resources.exercises
import fesb_companion_shared.shared.generated.resources.grade
import fesb_companion_shared.shared.generated.resources.is_taken
import fesb_companion_shared.shared.generated.resources.lectures
import fesb_companion_shared.shared.generated.resources.semester
import fesb_companion_shared.shared.generated.resources.status
import org.jetbrains.compose.resources.stringResource


@Composable
fun YearView(list: List<StudomatSubject>) {

    Column(
        modifier = Modifier
            .padding(24.dp, 12.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp, 24.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(Res.string.enrolled_subjects),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(12.dp, 4.dp, 12.dp, 16.dp)
        )
        SubjectsList(list)
    }
}

@Composable
fun SubjectsList(list: List<StudomatSubject>) {

    list.forEachIndexed { index, it ->
        val opened = remember { mutableStateOf(false) }
        if (index != 0) HorizontalDivider(
            Modifier.padding(horizontal = 12.dp),
            color = MaterialTheme.colorScheme.outline
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable { opened.value = !opened.value }
                .padding(12.dp, 8.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = it.name,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Left
            )
            Text(
                text = if (it.isPassed) it.grade else "-",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Right
            )
        }
        if (opened.value)
            Box(Modifier.noRippleClickable { opened.value = !opened.value }) { SubjectView(it) }
    }
}

@Composable
fun SubjectView(subject: StudomatSubject) {
    Column(
        Modifier.padding(14.dp, 8.dp, 14.dp, 20.dp)
    ) {
        SubjectText(text = stringResource(Res.string.elective_group), value = subject.electiveGroup)
        SubjectText(text = stringResource(Res.string.semester), value = subject.semester)
        SubjectText(text = stringResource(Res.string.lectures), value = subject.lectures)
        SubjectText(text = stringResource(Res.string.exercises), value = subject.exercises)
        SubjectText(text = stringResource(Res.string.ects_enrolled), value = subject.ectsEnrolled)
        SubjectText(text = stringResource(Res.string.is_taken), value = subject.isTaken)
        SubjectText(text = stringResource(Res.string.status), value = subject.status, isPassed = subject.isPassed)
        SubjectText(text = stringResource(Res.string.grade), value = subject.grade)
        SubjectText(text = stringResource(Res.string.exam_date), value = subject.examDate)
    }
}

@Composable
fun SubjectText(text: String, value: String, isPassed: Boolean = false) {
    val gradeModifier = if (isPassed) {
        Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(10.dp))
            .background(passGreen)
            .padding(8.dp, 4.dp, 16.dp, 4.dp)
    } else {
        Modifier
            .wrapContentSize()
            .padding(8.dp, 4.dp, 16.dp, 4.dp)
    }
    Row(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp, 0.dp, 8.dp, 0.dp),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Left
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            modifier = gradeModifier,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Right
        )
    }
}