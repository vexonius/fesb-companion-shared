package dev.etino.fcshared.compose

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import fesb_companion_shared.shared.generated.resources.Inter_VariableFont_opsz
import fesb_companion_shared.shared.generated.resources.Res
import org.jetbrains.compose.resources.Font


val InterFont
    @Composable // Composable is required for loading font from compose resources
    get() = FontFamily(
        Font(Res.font.Inter_VariableFont_opsz)
    )

val CustomTypography
    @Composable
    get() = Typography(
        displayLarge = TextStyle(
            // Heading 1
            fontFamily = InterFont,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 39.sp,
            color = Color.White,
        ),
        displayMedium = TextStyle( // Heading 2
            fontFamily = InterFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 29.sp,
            color = Color.White
        ),
        displaySmall = TextStyle( // Heading 3
            fontFamily = InterFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            color = Color.White
        ),
        headlineLarge = TextStyle( // Heading 4
            fontFamily = InterFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 19.sp,
            color = Color.White
        ),
        headlineMedium = TextStyle( // Heading 4
            fontFamily = InterFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 19.sp,
            color = Color.White
        ),
        headlineSmall = TextStyle( // Heading 5
            fontFamily = InterFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 17.sp,
            color = Color.White
        ),
        titleLarge = TextStyle( // Button 1
            fontFamily = InterFont,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            lineHeight = 22.sp,
            color = Color.White
        ),
        titleMedium = TextStyle( // Button 2
            fontFamily = InterFont,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 19.sp,
            color = Color.White
        ),
        titleSmall = TextStyle( // Button 3
            fontFamily = InterFont,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 17.sp,
            color = Color.White
        ),
        bodyLarge = TextStyle( // Body 1
            fontFamily = InterFont,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 19.sp,
            color = Color.White
        ),
        bodyMedium = TextStyle( // Body 2
            fontFamily = InterFont,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 17.sp,
            color = Color.White
        ),
        bodySmall = TextStyle( // Body 3
            fontFamily = InterFont,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 15.sp,
            color = Color.White
        ),
        labelLarge = TextStyle( // Label 1
            fontFamily = InterFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 19.sp,
            color = Color.White
        ),
        labelMedium = TextStyle( // Label 2
            fontFamily = InterFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 17.sp,
            color = Color.White
        ),
        labelSmall = TextStyle( // Label 3
            fontFamily = InterFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 15.sp,
            color = Color.White
        )
    )
