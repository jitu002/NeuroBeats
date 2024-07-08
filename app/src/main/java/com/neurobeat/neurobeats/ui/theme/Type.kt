package com.neurobeat.neurobeats.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.neurobeat.neurobeats.R

// Set of Material typography styles to start with
val clearLight=FontFamily(
    Font(R.font.clear_light_jlq)
)

val kodeMono= FontFamily(
    Font(R.font.kode_mono)
)

val museoModerno= FontFamily(
    Font(R.font.museo_moderno)
)

val youthTouch= FontFamily(
    Font(R.font.youth_touch)
)


val Typography = Typography(

    titleLarge = TextStyle(
        fontFamily = museoModerno,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = kodeMono,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),


)