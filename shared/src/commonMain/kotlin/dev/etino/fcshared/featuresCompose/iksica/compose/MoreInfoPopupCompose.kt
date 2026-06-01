package dev.etino.fcshared.featuresCompose.iksica.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex

@Composable
fun PopupBox(
    showPopup: Boolean,
    onClickOutside: () -> Unit,
    content: @Composable () -> Unit
) {
    if (showPopup) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                .zIndex(10F)
                .clickable {}, // da ne bi klinkilo kroz popup background na stvari ispod
            contentAlignment = Alignment.Center
        ) {
            Popup(
                alignment = Alignment.Center,
                properties = PopupProperties(),
                onDismissRequest = { onClickOutside() },
            ) {
                Box(
                    modifier = Modifier.shadow(elevation = 8.dp, shape = RoundedCornerShape(10.dp))
                ) {
                    Box(
                        Modifier
                            .wrapContentSize(align = Alignment.Center)
                            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(10.dp))
                            .clip(RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        content()
                    }
                }
            }
        }
    }
}