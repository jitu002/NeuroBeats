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

val BarColor=Color(0xFF1e0a35)

val txtColor = Color.White
val txtBgColor = listOf(Color.Transparent, Color.Black)