package com.neurobeat.neurobeats.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val CustomColor = Color(0xFF7D92FF)

val BackgroundColor = Brush.linearGradient(
    colors = listOf(
        Color(0xFF2b0339),
        Color(0xFF1e0a35),
        Color(0xFF130c2f),
        Color(0xFF0b0c27),
        Color(0xFF050a1e),
        Color(0xFF04091b),
        Color(0xFF030718),
        Color(0xFF020615),
        Color(0xFF040518),
        Color(0xFF08041a),
        Color(0xFF0d031c),
        Color(0xFF12011d)
    ),
    start = Offset(0f, 0f), // Top-left corner
    end = Offset.Infinite // Bottom-right corner
)
val profileColor=listOf(
    Color(0xFF2F4F4F),
    Color(0xFF556B2F),
    Color(0xFF006400),
    Color(0xFF00008B),
    Color(0xFF4B0082),
    Color(0xFF191970),
    Color(0xFF8B0000),
    Color(0xFFFF8C00),
    Color(0xFF9932CC),
    Color(0xFF8B4513),
    Color(0xFF483D8B),
    Color(0xFF800000),
    Color(0xFF8B008B),
    Color(0xFFB22222),
    Color(0xFFD2691E)
)

val BarColor=Color(0xFF1e0a35)

val txtColor = Color.White