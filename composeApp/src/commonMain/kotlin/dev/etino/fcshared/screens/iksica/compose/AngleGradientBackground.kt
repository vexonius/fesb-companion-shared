package dev.etino.fcshared.screens.iksica.compose

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

fun Modifier.angledGradientBackground(
    colors: List<Color>,
    degrees: Float,
    halfHalf: Boolean = false
) =
    drawBehind {
        var deg2 = degrees

        val (x, y) = size
        val gamma = atan2(y, x)

        if (halfHalf) {
            deg2 = atan2(x, y).times(180f / PI).toFloat()
        }

        if (gamma == 0f || gamma == (PI / 2).toFloat()) {
            return@drawBehind
        }

        val degreesNormalised = (deg2 % 360).let { if (it < 0) it + 360 else it }

        val alpha = (degreesNormalised * PI / 180).toFloat()

        val gradientLength = when (alpha) {
            in 0f..gamma, in (2 * PI - gamma)..2 * PI -> { x / cos(alpha) }

            in gamma..(PI - gamma).toFloat() -> { y / sin(alpha) }

            in (PI - gamma)..(PI + gamma) -> { x / -cos(alpha) }

            in (PI + gamma)..(2 * PI - gamma) -> { y / -sin(alpha) }

            else -> hypot(x, y)
        }

        val centerOffsetX = cos(alpha) * gradientLength / 2
        val centerOffsetY = sin(alpha) * gradientLength / 2

        drawRect(
            brush = Brush.linearGradient(
                colors = colors,
                start = Offset(center.x - centerOffsetX, center.y - centerOffsetY),
                end = Offset(center.x + centerOffsetX, center.y + centerOffsetY)
            ),
            size = size
        )
    }


