package dev.etino.fcshared.features.iksica.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember

@Composable
fun rememberNestedSheetState(
    sheetTopPadding: Float = 0f,
    composableHeight: Int = 0,
    sheetOffset: Int = 0
) = remember {
    NestedSheetState(
        sheetTopPadding = sheetTopPadding,
        composableHeight = mutableIntStateOf(composableHeight),
        sheetOffset = mutableIntStateOf(sheetOffset)
    )
}

class NestedSheetState(
    var sheetTopPadding: Float = 0f,
    var composableHeight: MutableIntState = mutableIntStateOf(0),
    var sheetOffset: MutableIntState = mutableIntStateOf(0),
    var set: Boolean = false
)