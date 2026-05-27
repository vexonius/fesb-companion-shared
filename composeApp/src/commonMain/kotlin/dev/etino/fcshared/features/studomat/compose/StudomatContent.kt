package dev.etino.fcshared.features.studomat.compose

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.etino.fcshared.compose.studomatBlue
import dev.etino.fcshared.studomat.models.StudomatYear
import dev.etino.fcshared.studomat.models.StudomatYearInfo
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun StudomatContent(studomatData: List<StudomatYear>, onClick: () -> Unit = {}) {
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        val list = studomatData.sortedByDescending { it.yearInfo.academicYear }
        val pageCount = list.size
        val pagerState = rememberPagerState(pageCount = { pageCount })

        list.getOrNull(pagerState.currentPage)?.yearInfo?.let { YearTitle(it) }
        DotIndicatorsStudomat(pageCount, pagerState)

        HorizontalPager(verticalAlignment = Alignment.Top, state = pagerState) { page ->
            Column(Modifier.wrapContentSize()) {
                YearView(list[page].subjects)
                Row(
                    Modifier
                        .padding(24.dp, 12.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .clickable { onClick() }
                        .background(MaterialTheme.colorScheme.background)
                        .padding(24.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.open_icon),
                        contentDescription = stringResource(Res.string.webview),
                        modifier = Modifier.padding(0.dp, 0.dp, 4.dp, 0.dp)
                    )
                    Text(
                        text = stringResource(Res.string.open_webview),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Left,
                    )
                }
            }
        }
    }
}


@Composable
fun YearTitle(yearInfo: StudomatYearInfo) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 0.dp)
        ) {
            // Empty text for vertical alignment purposes.
            Text(
                "\n",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
            )
            Text(
                text = yearInfo.courseName,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
            )
        }
        Text(
            text = yearInfo.academicYear,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(16.dp, 4.dp, 16.dp, 0.dp)
        )
    }
}

@Composable
fun DotIndicatorsStudomat(pageCount: Int, pagerState: PagerState) {
    val indicatorScrollState = rememberLazyListState()
    val colorUnselected = lerp(studomatBlue, Color.White, 0.5f)
    val colorSelected = Color.White

    LaunchedEffect(key1 = pagerState.currentPage, block = {
        val currentPage = pagerState.currentPage
        val size = indicatorScrollState.layoutInfo.visibleItemsInfo.size
        val lastVisibleIndex =
            indicatorScrollState.layoutInfo.visibleItemsInfo.last().index
        val firstVisibleItemIndex = indicatorScrollState.firstVisibleItemIndex

        if (currentPage > lastVisibleIndex - 1) {
            indicatorScrollState.animateScrollToItem(currentPage - size + 2)
        } else if (currentPage <= firstVisibleItemIndex + 1) {
            indicatorScrollState.animateScrollToItem((currentPage - 1).coerceAtLeast(0))
        }
    })
    Row(
        horizontalArrangement = Arrangement.Center, modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 8.dp, 0.dp, 8.dp)
    ) {
        LazyRow(
            state = indicatorScrollState,
            modifier = Modifier
                .height(30.dp)
                .width(((6 + 16) * 2 + 5 * (10 + 16)).dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) colorSelected else colorUnselected
                item(key = "item$iteration") {
                    val currentPage = pagerState.currentPage
                    val firstVisibleIndex by remember { derivedStateOf { indicatorScrollState.firstVisibleItemIndex } }
                    val lastVisibleIndex = indicatorScrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                    val size by animateDpAsState(
                        targetValue = when (iteration) {
                            currentPage -> {
                                10.dp
                            }
                            in firstVisibleIndex + 1..<lastVisibleIndex -> {
                                7.dp
                            }
                            else -> {
                                4.dp
                            }
                        }
                    )
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .background(color, CircleShape)
                            .size(size)
                    )
                }
            }
        }
    }
}