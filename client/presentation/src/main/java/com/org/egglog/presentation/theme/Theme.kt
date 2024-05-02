package com.org.egglog.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Warning300,
    onPrimary = Warning25,
    primaryContainer = Warning200,
    onPrimaryContainer = Warning500,

    secondary = NaturalBlack,
    onSecondary = NaturalWhite,
    secondaryContainer = Black,
    onSecondaryContainer = White,

    tertiary = Gray300,
    onTertiary = Gray25,
    tertiaryContainer = Gray200,
    onTertiaryContainer = Gray500,

    background = NaturalWhite,
    onBackground = NaturalBlack,

    surface = NaturalWhite,
    onSurface = NaturalBlack,

    surfaceVariant = Gray600,
    onSurfaceVariant = Gray900,
    outline = Gray700,

    error = Error500
)

private val LightColorScheme = lightColorScheme(
    primary = Warning300,
    onPrimary = Warning25,
    primaryContainer = Warning200,
    onPrimaryContainer = Warning500,

    secondary = NaturalBlack,
    onSecondary = NaturalWhite,
    secondaryContainer = Black,
    onSecondaryContainer = White,

    tertiary = Gray300,
    onTertiary = Gray25,
    tertiaryContainer = Gray200,
    onTertiaryContainer = Gray500,

    background = NaturalWhite,
    onBackground = NaturalBlack,

    surface = NaturalWhite,
    onSurface = NaturalBlack,

    surfaceVariant = Gray600,
    onSurfaceVariant = Gray900,
    outline = Gray700,

    error = Error500
)

@Composable
fun ClientTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if(darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}