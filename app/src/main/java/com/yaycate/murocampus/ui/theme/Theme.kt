package com.yaycate.murocampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaMuro = lightColorScheme(
    primary = AzulMarino,
    onPrimary = Color.White,
    primaryContainer = AzulClaro,
    onPrimaryContainer = AzulOscuro,
    secondary = AzulMarinoClaro,
    onSecondary = Color.White,
    surfaceTint = AzulMarino
)

@Composable
fun MuroCampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaMuro,
        typography = Typography,
        content = content
    )
}
