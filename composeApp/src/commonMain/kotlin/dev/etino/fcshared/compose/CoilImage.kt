package dev.etino.fcshared.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import coil3.compose.asPainter
import coil3.request.ImageRequest
import coil3.size.Precision
import coil3.size.Size


@Composable
fun CoilImage(url: String?, contentDescription: String, modifier: Modifier = Modifier) {
    var loading by remember { mutableStateOf(true) }
    Box {
        if (loading) {
            Column(
                modifier.background(Color.Black),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(trackColor = Color.White.copy(alpha = 0.3f), color = Color.White)
            }
        }

        val imageState = remember { mutableStateOf<Painter?>(null) }
        val context = LocalPlatformContext.current

        LaunchedEffect(url) {
            val request = ImageRequest.Builder(context)
                .data(url)
                .size(Size.ORIGINAL)
                .precision(Precision.EXACT)
                .build()
            val loader = SingletonImageLoader.get(context)
            val painter = loader.execute(request).image?.asPainter(context)
            imageState.value = painter
        }

        Image(
            imageState.value ?: ColorPainter(Color.Unspecified),
            contentDescription,
            modifier,
        )
    }
}
