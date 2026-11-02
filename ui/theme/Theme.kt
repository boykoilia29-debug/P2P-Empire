package com.grayscheme.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DarkBg = Color(0xFF0B1020)
val DeepBlue = Color(0xFF0F1B3D)
val Purple = Color(0xFF5A2DA6)
val Accent = Color(0xFF6C5CE7)
val Green = Color(0xFF00C853)
val Red = Color(0xFFE53935)
val Orange = Color(0xFFF7931A)

private val scheme = darkColorScheme(
    primary = Accent,
    background = DarkBg,
    surface = Color(0xFF151B33),
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun P2PTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = scheme, content = content)
}