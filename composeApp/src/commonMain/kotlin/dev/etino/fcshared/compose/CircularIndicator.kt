package dev.etino.fcshared.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Preview
@Composable
fun CircularIndicator(size: Dp = 45.dp) {
    Row(
        Modifier
            .fillMaxSize()
            .zIndex(1f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .clip(shape = CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .padding(size / 5)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(size),
                strokeWidth = size / 13,
                strokeCap = StrokeCap.Round
            )
        }
    }
}