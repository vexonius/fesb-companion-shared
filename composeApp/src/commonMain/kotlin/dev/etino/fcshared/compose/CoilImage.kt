package dev.etino.fcshared.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter


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
        AsyncImage(
            model = url,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.FillBounds,
            onState = { state ->
                when (state) {
                    is AsyncImagePainter.State.Loading -> {
                        loading = true
                    }

                    is AsyncImagePainter.State.Error -> {
                        // Optional: show error placeholder
                    }

                    is AsyncImagePainter.State.Success -> {
                        loading = false
                    }

                    else -> {}
                }
            }
        )
    }


}