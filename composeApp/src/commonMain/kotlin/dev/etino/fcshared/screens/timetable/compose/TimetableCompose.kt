package dev.etino.fcshared.screens.timetable.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusDays
import dev.etino.fcshared.compose.contentColors
import dev.etino.fcshared.compose.eventCardBackground
import dev.etino.fcshared.screens.timetable.TimetableViewModel
import dev.etino.fcshared.screens.timetable.compose.schedule.PositionedEvent
import dev.etino.fcshared.screens.timetable.compose.schedule.Schedule
import dev.etino.fcshared.screens.timetable.compose.schedule.SplitType
import dev.etino.fcshared.screens.timetable.compose.schedule.dayOfWeekHr
import dev.etino.fcshared.screens.timetable.compose.schedule.until
import dev.etino.fcshared.screens.timetable.utils.TimetableDateFormatter
import dev.etino.fcshared.timetable.Event
import dev.etino.fcshared.timetable.MonthData
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.cancelChoosingWeek
import fesb_companion_shared.composeapp.generated.resources.change_week
import fesb_companion_shared.composeapp.generated.resources.chooseChoosingWeek
import fesb_companion_shared.composeapp.generated.resources.tab_timetable
import fesb_companion_shared.composeapp.generated.resources.timetable_date_select_icon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.minusMonth
import kotlinx.datetime.plusMonth
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun TimetableCompose(timetableViewModel: TimetableViewModel, innerPaddingValues: PaddingValues) {

    val showDayEvent = timetableViewModel.currentEventShown
    val shownWeekChooseMenu = timetableViewModel.shownWeekChooseMenu.collectAsState(initial = false).value
    val lessonsToShow = timetableViewModel.events
    val shownWeek = timetableViewModel.mondayOfSelectedWeek
    //val daysInPeriods = timetableViewModel.daysInPeriods.value ?: emptyMap()
    val monthData = timetableViewModel.monthData
    val fetchUserTimetable = { selectedDate: LocalDate -> timetableViewModel.fetchUserTimetable(selectedDate) }
    val showEvent = { it: Event -> timetableViewModel.showEvent(it) }
    val showWeekChooseMenu = { it: Boolean -> timetableViewModel.showWeekChooseMenu(it) }
    val hideEvent = { timetableViewModel.hideEvent() }
    val snackbarHostState = SnackbarHostState()

    val sheetStateEvent = rememberModalBottomSheetState()
    val sheetStateCalendar = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val event = showDayEvent.collectAsState().value
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.RESUMED -> {
                timetableViewModel.resetToCurrentWeek()
                timetableViewModel.fetchUserTimetable()
            }

            else -> {}
        }
    }

    val message = timetableViewModel.showSnackbar.collectAsState().value?.let { stringResource(it) }
    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(message)
        }
    }
    Box(Modifier.padding(innerPaddingValues)) {
        BottomSheetScaffold(
            topBar = { TopAppBarTimetable { timetableViewModel.showWeekChooseMenu() } },
            containerColor = MaterialTheme.colorScheme.surface,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            sheetContent = {
                if (event != null) {
                    ModalBottomSheet(
                        sheetState = sheetStateEvent,
                        onDismissRequest = { hideEvent() },
                        contentWindowInsets = { WindowInsets(0.dp) },
                        dragHandle = { },
                    ) {
                        EventBottomSheet(event)
                    }
                } else if (shownWeekChooseMenu) {
                    ModalBottomSheet(
                        sheetState = sheetStateCalendar,
                        onDismissRequest = { showWeekChooseMenu(false) },
                        contentWindowInsets = { WindowInsets(0.dp) },
                        containerColor = MaterialTheme.colorScheme.surface,
                        dragHandle = { },
                    ) {
                        val coroutineScope = rememberCoroutineScope()
                        monthData.collectAsState().value.let {
                            BottomSheetCalendar(
                                monthData = it,
                                //daysInPeriods = daysInPeriods,
                                fetchUserTimetable = fetchUserTimetable,
                                coroutineScope = coroutineScope,
                                hideSheet = {
                                    coroutineScope.launch {
                                        sheetStateCalendar.hide()
                                        showWeekChooseMenu(false)
                                    }
                                }
                            )
                        }
                    }
                }
            },
            sheetPeekHeight = 0.dp,
        ) {
            val mapped = lessonsToShow.collectAsState(emptyList()).value
            val subExists: Boolean = mapped.any { it.start.dayOfWeek.ordinal == 6 }
            val eventBefore8AM = mapped.minByOrNull { it.start.time }
            val eventExistsBefore8AM =
                eventBefore8AM?.start?.time?.until(LocalTime(8, 0), DateTimeUnit.SECOND)?.let { it1 -> (it1 > 0) }
            val eventAfter8PM = mapped.maxByOrNull { it.end.time }
            val eventExistsAfter8PM = eventAfter8PM?.end?.time?.until(LocalTime(20, 0), DateTimeUnit.SECOND)?.let { it1 -> (it1 < 0) }
            val minTime = if (eventExistsBefore8AM == true) eventBefore8AM.start.time else LocalTime(8, 0)
            val maxTime = if (eventExistsAfter8PM == true) eventAfter8PM.end.time else LocalTime(20, 0)

            Schedule(
                events = mapped,
                eventContent = { posEvent ->
                    EventCard(
                        positionedEvent = posEvent,
                        onClick = { showEvent(posEvent.event) }
                    )
                },
                dayHeader = { day ->
                    DayHeader(day)
                },
                timeLabel = { time ->
                    SidebarLabel(time)
                },
                minTime = minTime,
                maxTime = maxTime,
                minDate = shownWeek.collectAsState().value,
                maxDate = (shownWeek.collectAsState().value).plusDays(if (subExists) 5 else 4),
                onClick = { click -> showEvent(click) },
            )
        }
    }
}

@OptIn(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun TopAppBarTimetable(showWeekChooseMenu: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(Res.string.tab_timetable),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.contentColors.primary,
            modifier = Modifier.padding(16.dp)
        )
        IconButton(
            onClick = { showWeekChooseMenu() },
            colors = IconButtonDefaults.iconButtonColors().copy(
                contentColor = Color.White
            )
        ) {
            Icon(
                painter = painterResource(Res.drawable.timetable_date_select_icon),
                contentDescription = stringResource(Res.string.change_week),
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun BottomSheetCalendar(
    monthData: MonthData,
    //daysInPeriods: Map<LocalDate, TimeTableInfo>,
    fetchUserTimetable: (LocalDate) -> Unit,
    hideSheet: () -> Unit,
    coroutineScope: CoroutineScope,
) {
    Column(
        Modifier
            .padding(8.dp, 8.dp, 8.dp, 20.dp), verticalArrangement = Arrangement.Top
    ) {
        var selection by remember {
            mutableStateOf<CalendarDay?>(
                CalendarDay(
                    LocalDate.now(),
                    DayPosition.MonthDate
                )
            )
        }
        val state = rememberCalendarState(
            startMonth = monthData.startMonth,
            endMonth = monthData.endMonth,
            firstVisibleMonth = monthData.currentMonth,
            firstDayOfWeek = monthData.firstDayOfWeek
        )
        SimpleCalendarTitle(
            modifier = Modifier.fillMaxWidth(),
            currentMonth = state.firstVisibleMonth.yearMonth,
            goToPrevious = {
                coroutineScope.launch {
                    state.scrollToMonth(state.firstVisibleMonth.yearMonth.minusMonth())
                }
            },
            goToNext = {
                coroutineScope.launch {
                    state.scrollToMonth(state.firstVisibleMonth.yearMonth.plusMonth())
                }
            },
        )
        Spacer(modifier = Modifier.padding(vertical = 5.dp))
        HorizontalCalendar(
            Modifier
                .heightIn(min = 350.dp), state = state, dayContent = { day ->
                Day(
                    day,
                    isSelected = selection == day,
                    //daysInPeriods = daysInPeriods
                ) { clicked ->
                    selection = clicked.takeUnless { it == selection }
                }
            })
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp, 16.dp)
        ) {
            TextButton(hideSheet) {
                Text(stringResource(Res.string.cancelChoosingWeek), color = MaterialTheme.contentColors.tertiary)
            }
            TextButton({
                selection?.let {
                    fetchUserTimetable(it.date)
                    hideSheet()
                }
            }) {
                Text(
                    stringResource(Res.string.chooseChoosingWeek),
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }
        }
    }
}

@Composable
fun SidebarLabel(
    time: LocalTime,
    modifier: Modifier = Modifier,
) {
    Text(
        text = time.format(TimetableDateFormatter.hourFormatter),
        textAlign = TextAlign.End,
        fontSize = 12.sp,
        lineHeight = 12.sp,
        modifier = modifier.fillMaxHeight(),
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
    )

}

@Composable
fun DayHeader(day: LocalDate) {
    val dayOfWeek = dayOfWeekHr(day.dayOfWeek.ordinal)
        .take(3)
        .lowercase()
        .replaceFirstChar { it.uppercase() }
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = day.format(TimetableDateFormatter.dayFormatter),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            modifier = Modifier,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        )
        Text(
            text = dayOfWeek,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            modifier = Modifier,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun EventCard(
    positionedEvent: PositionedEvent,
    modifier: Modifier = Modifier,
    onClick: (Event) -> Unit = {}
) {
    val event = positionedEvent.event
    val topRadius =
        if (positionedEvent.splitType == SplitType.Start || positionedEvent.splitType == SplitType.Both) 0.dp else 8.dp
    val bottomRadius =
        if (positionedEvent.splitType == SplitType.End || positionedEvent.splitType == SplitType.Both) 0.dp else 8.dp

    val shape = RoundedCornerShape(
        topStart = topRadius,
        topEnd = topRadius,
        bottomEnd = bottomRadius,
        bottomStart = bottomRadius,
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .border(width = 1.dp, color = event.color, shape = shape)
            .clip(shape)
            .background(color = eventCardBackground)
            .clickable { onClick(positionedEvent.event) }
            .padding(4.dp, 4.dp, 4.dp, 0.dp)
    ) {
        Text(
            text = event.name,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(0.7f, fill = false)
                .padding(bottom = 4.dp),
        )

        Text(
            text = event.classroom,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 10.sp,
            lineHeight = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(0.3f)
        )
    }
}


@Composable
fun Day(
    day: CalendarDay,
    isSelected: Boolean = false,
    //daysInPeriods: Map<LocalDate, TimeTableInfo>,
    onClick: (CalendarDay) -> Unit = {},
) {
    val inactiveTextColor = Color.DarkGray
    /*val dayInfo = daysInPeriods[day.date]
    val dayColor = dayInfo?.colorCode?.let { Color(it) } ?: Color.Transparent
    val backgroundColor = when (day.position) {
        DayPosition.MonthDate -> dayColor
        DayPosition.InDate, DayPosition.OutDate -> if (dayInfo?.category == "Bijela") Color.Transparent else dayColor.copy(
            alpha = 0.3f
        )
    }*/
    val backgroundColor = Color.Transparent
    val textColor = when (day.position) {
        DayPosition.MonthDate -> Color.Unspecified
        DayPosition.InDate, DayPosition.OutDate -> inactiveTextColor
    }
    Column {
        Column(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(CircleShape)
                .drawBehind {
                    drawCircle(
                        color = backgroundColor,
                        radius = size.width / 4,
                        center = center
                    )
                }
                .clickable { onClick(day) }
                .border(2.dp, if (isSelected) Color.White else Color.Transparent, CircleShape)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                fontWeight = FontWeight.Medium,
                color = textColor,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}