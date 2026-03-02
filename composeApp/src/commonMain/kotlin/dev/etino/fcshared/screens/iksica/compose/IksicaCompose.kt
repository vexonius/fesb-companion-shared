package dev.etino.fcshared.screens.iksica.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.etino.fcshared.compose.contentColors
import dev.etino.fcshared.iksica.models.IksicaData
import dev.etino.fcshared.iksica.models.Receipt
import dev.etino.fcshared.screens.home.compose.noRippleClickable
import dev.etino.fcshared.screens.iksica.view.IksicaReceiptState
import dev.etino.fcshared.screens.iksica.view.IksicaViewModel
import dev.etino.fcshared.screens.iksica.view.IksicaViewState
import fesb_companion_shared.composeapp.generated.resources.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@OptIn(
    InternalCoroutinesApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
fun IksicaCompose(iksicaViewModel: IksicaViewModel, innerPaddingValues: PaddingValues) {

    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val listState = rememberLazyListState()
    val nestedSheetState = rememberNestedSheetState(composableHeight = 999, sheetOffset = 999)

    val receiptSelected = iksicaViewModel.receiptSelected.collectAsState().value
    val iksicaData = iksicaViewModel.iksicaData.collectAsState().value
    val viewState = iksicaViewModel.viewState.collectAsState().value ?: IksicaViewState.Loading

    val isRefreshing = viewState is IksicaViewState.Fetching || viewState is IksicaViewState.Loading
    val showPopup = remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(isRefreshing, { iksicaViewModel.getReceipts() })
    val snackbarHostState = SnackbarHostState()
    val message = iksicaViewModel.showSnackbar.collectAsState().value?.let { stringResource(it) }


    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(message)
        }
    }

    LaunchedEffect(lifecycleState) {
        if (lifecycleState == Lifecycle.State.RESUMED) iksicaViewModel.getReceipts()
    }

    DisposableEffect(lifecycleState) {
        onDispose {
            if (lifecycleState == Lifecycle.State.CREATED) {
                coroutineScope.launch {
                    listState.scrollToItem(0)
                }
            }
        }
    }
    BottomSheetScaffold(
        sheetPeekHeight = 0.dp,
        modifier = Modifier
            .pullRefresh(pullRefreshState)
            .nestedScroll(TopAppBarDefaults.pinnedScrollBehavior().nestedScrollConnection),
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        sheetContent = {
            if (receiptSelected is IksicaReceiptState.Success)
                BottomSheetIksica(receiptSelected.data) { iksicaViewModel.hideReceiptDetails() }
        }) {
        Box(Modifier.fillMaxWidth().padding(innerPaddingValues)) {
            PullRefreshIndicator(
                isRefreshing, pullRefreshState, Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(5f)
            )

            when (viewState) {
                is IksicaViewState.Initial, is IksicaViewState.Empty -> EmptyIksicaView()
                is IksicaViewState.Success -> {
                    PopulatedIksicaView(
                        viewState.data,
                        listState,
                        nestedSheetState,
                        onCardClick = { showPopup.value = true },
                        onItemClick = { iksicaViewModel.getReceiptDetails(it) }
                    )
                }

                is IksicaViewState.Fetching -> {
                    PopulatedIksicaView(
                        viewState.data,
                        listState,
                        nestedSheetState,
                        onCardClick = { showPopup.value = true },
                        onItemClick = { iksicaViewModel.getReceiptDetails(it) }
                    )
                }

                else -> {}
            }
        }
    }

    PopupBox(showPopup = showPopup.value, onClickOutside = { showPopup.value = !showPopup.value }) {
        iksicaData?.studentData?.let { CardIksicaPopupContent(it) }
    }
}

@OptIn(InternalCoroutinesApi::class)
@Composable
fun EmptyIksicaView() {
    Column {
        TopBarIksica()
        Box(Modifier.fillMaxWidth()) {
            LazyColumn(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    EmptyIksicaContent(stringResource(Res.string.iksica_no_data))
                }
            }
        }
    }
}

@OptIn(InternalCoroutinesApi::class)
@Composable
fun PopulatedIksicaView(
    model: IksicaData,
    listState: LazyListState,
    nestedSheetState: NestedSheetState,
    onCardClick: () -> Unit,
    onItemClick: (Receipt) -> Unit
) {
    val sheetOffset = nestedSheetState.sheetOffset
    val sheetTopPadding = nestedSheetState.sheetTopPadding
    val composableHeight = nestedSheetState.composableHeight

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                if (listState.firstVisibleItemIndex == 0) {
                    sheetOffset.intValue =
                        (sheetOffset.intValue + delta).coerceIn(sheetTopPadding, composableHeight.intValue.toFloat())
                            .toInt()
                }
                return Offset(
                    0f,
                    if (composableHeight.intValue > sheetOffset.intValue && sheetOffset.intValue > sheetTopPadding) delta else 0f
                )
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ) =
                Offset(0f, if (sheetOffset.intValue == 0) available.y else 0f)
        }
    }

    Box(modifier = Modifier.nestedScroll(nestedScrollConnection)) {
        Column(Modifier.onGloballyPositioned {
            if (!nestedSheetState.set) {
                nestedSheetState.set = true
                composableHeight.intValue = it.size.height
                sheetOffset.intValue = it.size.height
            }
        }) {
            TopBarIksica()

            Box(Modifier.fillMaxWidth()) {
                ElevatedCardIksica(
                    model.studentData.nameSurname,
                    model.studentData.cardNumber,
                    model.studentData.balanceInCents
                ) {
                    onCardClick()
                }
            }
        }
        Column(
            modifier = Modifier
                .offset { IntOffset(0, sheetOffset.intValue) }
                .clip(RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp))
                .background(MaterialTheme.colorScheme.surface)
                .noRippleClickable {}
        ) {
            val receipts = model.receipts
            if (receipts.isNullOrEmpty()) {
                EmptyIksicaContent(stringResource(Res.string.iksica_no_receipts))
            } else {
                TransactionsText()
                LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                    items(receipts) {
                        IksicaItem(it) { onItemClick(it) }
                    }
                }
            }
        }
    }
}

@OptIn(InternalCoroutinesApi::class)
@Composable
fun TopBarIksica() {
    Row(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(Res.string.tab_iksica),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.contentColors.primary,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun TransactionsText() {
    Text(
        text = stringResource(Res.string.transactions),
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = MaterialTheme.contentColors.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 30.dp, 16.dp, 24.dp)
    )
}

@Composable
fun EmptyIksicaContent(text: String) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp, 100.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = text,
            color = MaterialTheme.contentColors.secondary
        )
    }
}