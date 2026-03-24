package com.example.x5tech.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = MistBlue,
    onPrimary = Night,
    secondary = Copper,
    tertiary = Forest,
    background = Night,
    surface = Color(0xFF142133),
    surfaceVariant = Color(0xFF203148),
    onSurface = Color(0xFFEAF2F8),
    onSurfaceVariant = Color(0xFFB8C7D6),
    error = RoseMist,
)

private val LightColorScheme = lightColorScheme(
    primary = InkBlue,
    onPrimary = Color.White,
    secondary = Copper,
    tertiary = Forest,
    background = Paper,
    surface = Color.White,
    surfaceVariant = MistBlue,
    onSurface = Night,
    onSurfaceVariant = Slate,
    outline = Color(0xFFC8D3DD),
    error = Color(0xFFB42318),
)

@Composable
internal fun X5techTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
