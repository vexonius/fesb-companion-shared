package dev.etino.fcshared.features.studomat.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import com.multiplatform.webview.web.rememberWebViewState
import dev.etino.fcshared.compose.studomatBlue
import dev.etino.fcshared.features.studomat.view.StudomatViewModel
import fesb_companion_shared.composeapp.generated.resources.Res
import fesb_companion_shared.composeapp.generated.resources.tab_studomat
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun StudomatCompose(studomatViewModel: StudomatViewModel, innerPaddingValues: PaddingValues) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        studomatViewModel.showSnackbar.collect { resId ->
            snackbarHostState.showSnackbar(
                message = getString(resId)
            )
        }
    }

    val studomatData = studomatViewModel.studomatData.collectAsState().value
    val isRefreshing = studomatViewModel.isRefreshing.collectAsState().value
    val pullRefreshState = rememberPullRefreshState(isRefreshing, {
        studomatViewModel.getStudomatData(pulldownTriggered = true)
    })
    val openedWebview = remember { mutableStateOf(false) }

    val lifecycleState = LocalLifecycleOwner.current.lifecycle.currentStateAsState().value
    LaunchedEffect(lifecycleState) {
        if (lifecycleState == Lifecycle.State.RESUMED) {
            studomatViewModel.getStudomatData()
        }
    }

    Scaffold(
        modifier = Modifier.pullRefresh(pullRefreshState)
            .background(Brush.verticalGradient(listOf(studomatBlue, Color.Transparent)))
            .padding(innerPaddingValues),
        contentWindowInsets = WindowInsets(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                //.background(Brush.verticalGradient(listOf(studomatBlue, Color.Transparent)))
                .padding(innerPadding),
        ) {
            val webViewState =
                rememberWebViewState("https://www.isvu.hr/studomat/hr/ispit/ponudapredmetazaprijavuispita")
            val cookie = studomatViewModel.studomatCookie.collectAsState().value
            LaunchedEffect(cookie) {
                cookie?.let {
                    webViewState.cookieManager.setCookie(
                        "https://www.isvu.hr/studomat", it
                    )
                }
            }
            if (openedWebview.value) {
                BackHandler { openedWebview.value = false }
                WebViewScreen(webViewState)
                return@Scaffold
            }
            PullRefreshIndicator(
                isRefreshing,
                pullRefreshState,
                Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(2f),
                scale = true
            )
            Column {
                Text(
                    text = stringResource(Res.string.tab_studomat),
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(16.dp)
                )
                if (!studomatData.isNullOrEmpty()) {
                    StudomatContent(studomatData, onClick = { openedWebview.value = true })
                } else {
                    EmptyStudomatView()
                }
            }
        }
    }
}