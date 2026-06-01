package dev.etino.fcshared.featuresCompose.menza.view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import dev.etino.fcshared.featuresKotlin.menza.models.menzaLocations
import kotlinx.coroutines.InternalCoroutinesApi

@OptIn(ExperimentalMaterial3Api::class, InternalCoroutinesApi::class)
@Composable
fun MenzaCompose(menzaViewModel: MenzaViewModel) {

    val lifecycleState = LocalLifecycleOwner.current.lifecycle.currentStateAsState().value
    val imageUrl = menzaViewModel.images.collectAsState().value
    val menzas = menzaViewModel.menza.collectAsState().value

    Surface(modifier = Modifier.fillMaxSize()) {
        val pageCount = menzaLocations.size
        val state = rememberPagerState(
            initialPage = (pageCount.div(2)),
            pageCount = { pageCount }
        )
        DisposableEffect(lifecycleState) {
            onDispose {
                menzaViewModel.closeMenza()
            }
        }
        LaunchedEffect(state.settledPage) {
            menzaViewModel.updateMenzaUrl(menzaLocations[state.settledPage])
        }
        val indicatorScrollState = rememberLazyListState()

        LaunchedEffect(key1 = state.currentPage, block = {
            val currentPage = state.currentPage
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
        Column {
            Row(
                horizontalArrangement = Arrangement.Center, modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(0.dp, 16.dp, 0.dp, 16.dp)
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
                        val color = if (state.currentPage == iteration) Color.White else Color.LightGray
                        item(key = "item$iteration") {
                            val currentPage = state.currentPage
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
            HorizontalPager(state, pageSpacing = 16.dp) {
                val meni = menzas?.getOrNull(it)
                val imgUrl = if (imageUrl?.first == meni?.first) imageUrl?.second else null
                ImageMeniView(menzaViewModel, imgUrl, meni)
            }
        }
    }
}