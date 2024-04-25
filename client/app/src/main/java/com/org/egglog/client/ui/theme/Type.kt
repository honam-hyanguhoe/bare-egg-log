package com.org.egglog.client.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.org.egglog.client.R

val lineFontFamily = FontFamily(
    Font(R.font.line_seed_thin, FontWeight.Thin),
    Font(R.font.line_seed_normal, FontWeight.Normal),
    Font(R.font.line_seed_semi_bold, FontWeight.SemiBold),
    Font(R.font.line_seed_bold, FontWeight.Bold),
    Font(R.font.line_seed_extra_bold, FontWeight.ExtraBold),
)

val Typography = Typography(
    // h1
    titleLarge = TextStyle(
        fontFamily = lineFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp
    ),
    // h2
    headlineLarge = TextStyle(
        fontFamily = lineFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 22.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.5.sp
    ),
    // h3
    headlineMedium = TextStyle(
        fontFamily = lineFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp
    ),
    // h4
    headlineSmall = TextStyle(
        fontFamily = lineFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.5.sp
    ),

    // body1 bold
    bodyLarge = TextStyle(
        fontFamily = lineFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    // body1 regular
    bodyMedium = TextStyle(
        fontFamily = lineFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    // body2 bold
    displayLarge = TextStyle(
        fontFamily = lineFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    ),
    // body2 regular
    displayMedium = TextStyle(
        fontFamily = lineFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    ),

    // detail bold
    labelLarge = TextStyle(
        fontFamily = lineFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    // detail regular
    labelMedium = TextStyle(
        fontFamily = lineFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),

    // font-size-xxxs Bold
    labelSmall = TextStyle(
        fontFamily = lineFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.2.sp
    )

)