package com.chondosha.flowerfinder.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = DarkRed,
    primaryVariant = DarkRedVariant,
    secondary = DarkBlue,
    secondaryVariant = DarkBlueVariant,
    background = DarkBackground,
    onSurface = DarkTextPrimary,
    onPrimary = DarkTextPrimary,
    onSecondary = DarkTextSecondary,
    onBackground = DarkTextPrimary,
)

private val LightColorPalette = lightColors(
    primary = LightRed,
    primaryVariant = LightRedVariant,
    secondary = LightBlue,
    secondaryVariant = LightBlueVariant,
    background = LightBackground,
    onSurface = LightBackground,
    onPrimary = LightTextPrimary,
    onSecondary = LightTextSecondary,
    onBackground = LightTextBackground,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun FlowerFinderTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}