package dev.etino.fcshared.features.menza.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import dev.etino.fcshared.compose.CoilImage

enum class Orientation { PORTRAIT, LANDSCAPE_LEFT, LANDSCAPE_RIGHT }

@Composable
fun Rotatable90Image(imageUrl: String?, contentDescription: String) {

    val ratio = 627 / 353f

    val scale = remember { mutableFloatStateOf(1f) }
    val rotationState = remember { mutableFloatStateOf(0f) }
    val aspectRatio = remember { mutableFloatStateOf(ratio) }
    val manualRotation = remember { mutableStateOf(false) }
    val orientation = remember { mutableStateOf(Orientation.PORTRAIT) }

    fun rotate(to: Orientation = orientation.value) {
        if (to == Orientation.PORTRAIT) {
            orientation.value = Orientation.PORTRAIT
            aspectRatio.floatValue = ratio
            rotationState.floatValue = 0f
            scale.floatValue = 1f
        } else {
            if (to == Orientation.LANDSCAPE_LEFT) {
                orientation.value = Orientation.LANDSCAPE_LEFT
                rotationState.floatValue = 90f
            } else {
                orientation.value = Orientation.LANDSCAPE_RIGHT
                rotationState.floatValue = -90f
            }
            aspectRatio.floatValue = 1 / ratio
            scale.floatValue = ratio
        }
    }

    CoilImage(
        url = imageUrl, contentDescription = contentDescription, modifier = Modifier
            .aspectRatio(aspectRatio.floatValue.coerceIn(1 / ratio..ratio))
            .animateContentSize()
            .graphicsLayer(
                scaleX = scale.floatValue,
                scaleY = scale.floatValue,
                rotationZ = rotationState.floatValue,
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                manualRotation.value = !manualRotation.value
                rotate(
                    if (manualRotation.value) Orientation.LANDSCAPE_LEFT
                    else Orientation.PORTRAIT
                )
            })
}